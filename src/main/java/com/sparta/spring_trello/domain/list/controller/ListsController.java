package com.sparta.spring_trello.domain.list.controller;

import com.sparta.spring_trello.domain.list.dto.request.ListsRequestDto;
import com.sparta.spring_trello.domain.list.dto.response.ListsResponseDto;
import com.sparta.spring_trello.domain.list.service.ListsService;
import com.sparta.spring_trello.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* 리스트 관련 API 요청을 처리하는 컨트롤러.
 * 멤버 권환 확인 해야함*/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class ListsController {

    private ListsService listsService;

    /**
     * 특정 보드에 리스트를 생성합니다.
     *
     * @param boardId 보드 ID
     * @param title   리스트 제목
     * @return 생성된 리스트에 대한 응답 DTO(boardId,id,title,order)
     */
    @PostMapping("{boardId}/lists")
    public ResponseEntity<ApiResponse<ListsResponseDto>> createList(
            @RequestBody String email,
            @PathVariable Long boardId,
            @RequestBody String title) {
        return ResponseEntity.ok(ApiResponse.success(listsService.createList(email, boardId, title)));
    }

    /**
     * 특정 리스트를 수정합니다.
     *
     * @param boardId         보드 ID
     * @param listsId         수정할 리스트 ID
     * @param listsRequestDto 수정할 리스트의 요청 DTO(title,order)
     * @return 수정된 리스트에 대한 응답 DTO
     */
    @PutMapping("{boardId}/lists/{listsId}")
    public ResponseEntity<ApiResponse<ListsResponseDto>> updateList(
            @RequestBody String email,
            @PathVariable Long boardId,
            @PathVariable Long listsId,
            @RequestBody ListsRequestDto listsRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(listsService.updateList(email, boardId, listsId, listsRequestDto)));
    }

    /**
     * 특정 리스트를 삭제합니다.
     *
     * @param boardId 보드 ID
     * @param listsId 삭제할 리스트 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("{boardId}/lists/{listsId}")
    public ResponseEntity<ApiResponse<?>> deleteList(
            @RequestBody String email,
            @PathVariable Long boardId,
            @PathVariable Long listsId) {
        listsService.deleteLists(email, boardId, listsId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
