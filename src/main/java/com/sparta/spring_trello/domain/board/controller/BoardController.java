package com.sparta.spring_trello.domain.board.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.board.dto.request.BoardRequestDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardSimpleResponseDto;
import com.sparta.spring_trello.domain.board.service.BoardService;
import com.sparta.spring_trello.domain.list.dto.request.BoardUpdateRequestDto;
import com.sparta.spring_trello.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     * @return 생성된 보드에 대한 응답 DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponseDto>> createBoard(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody BoardRequestDto boardRequestDto
    ) {
        return ResponseEntity.ok(ApiResponse.success(boardService.createBoard(user, boardRequestDto)));
    }

    /**
     * 사용자에 해당하는 모든 보드를 조회합니다.
     *
     * @return 사용자의 모든 보드에 대한 응답 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardSimpleResponseDto>>> getBoards(
            @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoards(user)));
    }

    /**
     * 특정 보드의 상세 정보를 조회합니다.
     *
     * @param boardId 조회할 보드 ID
     * @return 특정 보드에 대한 상세 응답 DTO
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> getBoard(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoard(user, boardId)));
    }

    /**
     * 특정 보드를 수정합니다.
     *
     * @param boardId 삭제할 보드 ID
     * @return 수정된 값 반환
     * @Param BoardUpdateRequestDto 생성할 보드이 요청 DTO
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable("boardId") Long boardId,
            @RequestBody BoardUpdateRequestDto RequestDto) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateBoard(user, boardId, RequestDto)));
    }

    /**
     * 특정 보드를 삭제합니다.
     *
     * @param boardId 삭제할 보드 ID
     * @return 삭제 성공 응답
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> deleteBoard(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(user, boardId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
