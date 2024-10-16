package com.sparta.spring_trello.domain.board.controller;

import com.sparta.spring_trello.domain.board.dto.request.BoardRequestDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardSimpleResponseDto;
import com.sparta.spring_trello.domain.board.service.BoardService;
import com.sparta.spring_trello.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
 *  보드 관련 API 요청을 처리하는 컨트롤러.
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 새로운 보드를 생성합니다.
     *
     * @param boardRequestDto 생성할 보드의 요청 DTO
     * @param backgroundColor 선택적 배경 색상
     * @param image           선택적 이미지 URL
     * @return 생성된 보드에 대한 응답 DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponseDto>> createBoard(
            @RequestBody String email,
            @RequestBody BoardRequestDto boardRequestDto,
            @RequestBody(required = false) String backgroundColor,
            @RequestBody(required = false) String image
    ) {
        return ResponseEntity.ok(ApiResponse.success(boardService.createBoard(email, boardRequestDto, backgroundColor, image)));
    }

    /**
     * 사용자 이메일에 해당하는 모든 보드를 조회합니다.
     *
     * @param email 사용자의 이메일
     * @return 사용자의 모든 보드에 대한 응답 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardSimpleResponseDto>>> getBoards(@RequestBody String email) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoards(email)));
    }

    /**
     * 특정 보드의 상세 정보를 조회합니다.
     *
     * @param email   사용자의 이메일
     * @param boardId 조회할 보드 ID
     * @return 특정 보드에 대한 상세 응답 DTO
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> getBoard(
            @RequestBody String email,
            @PathVariable Long boardId) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoard(email, boardId)));
    }

    /**
     * 특정 보드를 수정합니다.
     *
     * @param email           사용자의 이메일
     * @param boardId         삭제할 보드 ID
     * @param title           변경 할 제목
     * @param backgroundColor 변경 할 배경 컬러
     * @param image           변경할 이미지
     * @return 수정된 값 반환
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> updateBoard(
            @RequestBody String email,
            @PathVariable Long boardId,
            @RequestBody String title,
            @RequestBody(required = false) String backgroundColor,
            @RequestBody(required = false) String image) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateBoard(email, boardId, title, backgroundColor, image)));
    }

    /**
     * 특정 보드를 삭제합니다.
     *
     * @param email   사용자의 이메일
     * @param boardId 삭제할 보드 ID
     * @return 삭제 성공 응답
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> deleteBoard(@RequestBody String email, @PathVariable Long boardId) {
        boardService.deleteBoard(email, boardId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
