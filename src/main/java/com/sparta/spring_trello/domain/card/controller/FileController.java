package com.sparta.spring_trello.domain.card.controller;

import com.sparta.spring_trello.domain.card.service.FileService;
import com.sparta.spring_trello.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cards/{cardId}/attachments")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 카드에 파일 첨부 추가
     * @param cardId 카드 ID
     * @param file   첨부 파일
     * @return 첨부 파일 URL
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addAttachment(
            @PathVariable Long cardId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String uploadedUrl = fileService.uploadFile(file, cardId);
        return ResponseEntity.ok(ApiResponse.success(uploadedUrl));
    }

    /**
     * 카드 ID에 연결된 파일 조회
     * @param cardId 카드 ID
     * @return 첨부된 파일 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAttachmentsByCardId(
            @PathVariable Long cardId
    ) {
        List<String> files = fileService.listFilesByCardId(cardId);
        return ResponseEntity.ok(ApiResponse.success(files));
    }

    /**
     * 첨부된 파일 삭제
     * @param cardId 카드 ID
     * @param attachmentId 첨부 파일 ID
     * @return 처리 결과
     */
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(
            @PathVariable Long cardId,
            @PathVariable String attachmentId
    ) {
        fileService.deleteFile(cardId, attachmentId);
        return ResponseEntity.ok().build();
    }
}
