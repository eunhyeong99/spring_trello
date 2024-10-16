package com.sparta.spring_trello.domain.list.service;

import com.sparta.spring_trello.domain.board.repository.BoardRepository;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import com.sparta.spring_trello.domain.list.dto.request.ListsRequestDto;
import com.sparta.spring_trello.domain.list.dto.response.ListsResponseDto;
import com.sparta.spring_trello.domain.list.entity.Lists;
import com.sparta.spring_trello.domain.list.repository.ListsRepository;
import com.sparta.spring_trello.domain.member.repository.MemberRepository;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListsService { /*멤버 권환 확인 해야함*/

    private final ListsRepository listsRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    // 리스트 생성
    @Transactional
    public ListsResponseDto createList( long boardId, String title) {
//        isValidUserMemberRole(email);
        isValidBoardId(boardId); // 보드 아이디 검증

        int maxOrder = listsRepository.findMaxOrder().orElse(0); // 순서 초기 적용

        Lists newlists = new Lists(title, maxOrder + 1); // 제일 큰 순서 +1 로 순서 부여
        listsRepository.save(newlists); // 리스트 저장
        return ListsResponseDto.from(newlists);
    }

    // 리스트 수정
    @Transactional
    public ListsResponseDto updateList(Long boardId, Long listsId, ListsRequestDto listsRequestDto) {
//        isValidUserMemberRole(email);
        isValidBoardId(boardId); // 보드 아이디 검증
        Lists currentList = getListsById(listsId); // 리스트 아이디 검증
        Integer currentOrder = currentList.getOrder(); // 리스트 순서

        currentList.updateLists(listsRequestDto.getTitle(), listsRequestDto.getOrder()); // 순서가 같으면 제목만 바꿔서 반영

        if (!Objects.equals(listsRequestDto.getOrder(), currentOrder)) {
            if (currentOrder > listsRequestDto.getOrder()) { // 순서가 바꾸려는 순서보다 크면 해당 순서로 변경 후 다른 순서들은 1씩 줄임
                listsRepository.findAllByOrderBetween(currentOrder + 1, listsRequestDto
                                .getOrder())
                        .forEach(l -> l.updateLists(listsRequestDto.getTitle(), l.getOrder() - 1));
                currentList.updateLists(listsRequestDto.getTitle(), listsRequestDto.getOrder());
            } else {
                listsRepository.findAllByOrderBetween(currentOrder + 1, listsRequestDto // 순서가 바꾸려는 순서보다 작으면 해당 순서로 변경 후 다른 순서들은 1씩 더함
                                .getOrder())
                        .forEach(l -> l.updateLists(listsRequestDto.getTitle(), l.getOrder() + 1));
                currentList.updateLists(listsRequestDto.getTitle(), listsRequestDto.getOrder());
            }
        }
        listsRepository.save(currentList);
        return ListsResponseDto.from(currentList);
    }

    // 리스트 삭제
    @Transactional
    public void deleteLists( Long boardId, Long listsId) {
//        isValidUserMemberRole(email);
        isValidBoardId(boardId); // 보드 아이디 검증
        Lists lists = getListsById(listsId); // 리스트 아이디 검증
        Integer currentOrder = lists.getOrder(); // 리스트 순서 확인

        listsRepository.delete(lists); // 리스트 삭제

        List<Lists> listToUpdate = listsRepository.findAllByIdAndOrderGreaterThan(listsId, currentOrder);
        for (Lists list : listToUpdate) { // 리스트 삭제 후 해당 순서 삭제 후 보다 큰 순서들도 1씩 줄임
            list.updateLists(list.getTitle(), lists.getOrder() - 1);
            listsRepository.save(list);
        }
    }

    // 유저 이메일 검증
//    public void isValidUserMemberRole(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//        Member member = memberRepository.findByUserId(user.getId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_READ_ONLY));
//        if (MemberRole.READONLY.equals(member.getMemberRole())) {
//            throw new CustomException(ErrorCode.MEMBER_READ_ONLY);
//        }
//    }

    // 보드 아이디 검증
    public void isValidBoardId(Long boardId) {
        boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    // 리스트 아이디 검증
    public Lists getListsById(Long listsId) {
        return listsRepository.findById(listsId).orElseThrow(() ->
                new CustomException(ErrorCode.LISTS_NOT_FOUND));
    }

}
