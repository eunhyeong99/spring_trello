package com.sparta.spring_trello.domain.card.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, Long cardId) throws IOException {
        checkPermission(); // 권한 확인
        validateFile(file); // 파일 형식 및 크기 검증

        // 파일 이름 중복 방지를 위해 UUID 사용
        String fileName = cardId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 메타데이터 설정 (파일 크기)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            // S3에 파일 업로드
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (SdkClientException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_UPLOAD, "파일 업로드 중 오류가 발생했습니다.");
        }

        // 업로드된 파일의 S3 URL 반환
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    // 파일 조회
    @Transactional(readOnly = true)
    public List<String> listFilesByCardId(Long cardId) {
        checkPermission(); // 권한 확인

        String prefix = cardId + "_"; // 카드 ID를 포함하는 파일만 필터링
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix);
        ListObjectsV2Result result = s3Client.listObjectsV2(req);

        // 결과에서 실제 파일 이름만 추출
        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    // 파일 삭제
    public void deleteFile(Long cardId, String fileName) {
        checkPermission(); // 권한 확인

        // 카드 ID와 파일 이름이 일치하는지 확인
        if (!fileName.startsWith(cardId + "_")) {
            throw new CustomException(ErrorCode.NOT_FOUND_FILE,"삭제하려는 파일이 존재하지 않습니다.");
        }

        if (!s3Client.doesObjectExist(bucketName, fileName)) {
            throw new CustomException(ErrorCode.NOT_FOUND_FILE,"삭제하려는 파일이 존재하지 않습니다.");
        }

        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (SdkClientException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_DELETE,"파일 삭제 중 오류가 발생했습니다.");
        }
    }

    // 파일 형식 및 크기 검증
    private void validateFile(MultipartFile file) {
        // 파일 크기 제한 (5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new CustomException(ErrorCode.PAYLOAD_TOO_LARGE, "파일 사이즈가 5MB를 초과합니다.");
        }

        // 허용되는 파일 형식
        String contentType = file.getContentType();
        assert contentType != null;
        if (!isSupportedContentType(contentType)) {
            throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_TYPE, "지원되지 않는 파일 형식입니다.");
        }
    }

    // 지원되는 파일 형식 확인
    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf") ||
                contentType.equals("text/csv");
    }

    // 권한 확인 메서드
    private void checkPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isReadOnly = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("READONLY"));

        if (isReadOnly) {
            throw new CustomException(ErrorCode.READ_ONLY_USER_ERROR, "읽기 전용 사용자는 파일 업로드/삭제를 할 수 없습니다.");
        }
    }
}
