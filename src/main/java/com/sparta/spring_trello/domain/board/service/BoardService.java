package com.sparta.spring_trello.domain.board.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.board.dto.request.BoardRequestDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardSimpleResponseDto;
import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.board.repository.BoardRepository;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import com.sparta.spring_trello.domain.list.dto.request.BoardUpdateRequestDto;
import com.sparta.spring_trello.domain.list.entity.Lists;
import com.sparta.spring_trello.domain.list.repository.ListsRepository;
import com.sparta.spring_trello.domain.member.entity.Member;
import com.sparta.spring_trello.domain.member.entity.MemberRole;
import com.sparta.spring_trello.domain.member.repository.MemberRepository;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final ListsRepository listsRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final WorkspaceRepository workspaceRepository;

    // 보드 생성
    @Transactional
    public BoardResponseDto createBoard(AuthUser user, BoardRequestDto boardRequestDto) {
        isValidUserMemberRole(user);
        Workspace workspace = isValidWorkspace(boardRequestDto.getWorkspaceId());

        // 배경색과 이미지 중 하나는 반드시 존재해야 함
        if ((boardRequestDto.getBackgroundColor() == null || boardRequestDto.getBackgroundColor().isEmpty()) &&
                (boardRequestDto.getImage() == null || boardRequestDto.getImage().isEmpty())) {
            throw  new CustomException(ErrorCode.BOARD_NON_BACK_GROUD);
        }
        Board newboard = new Board(
                workspace,
                boardRequestDto.getTitle(),
                boardRequestDto.getBackgroundColor(),
                boardRequestDto.getImage());
        Board savedBoard = boardRepository.save(newboard);
        return BoardResponseDto.from(savedBoard);
    }

    // 본인의 모든 워크스페이스 내 보드들 조회
    public List<BoardSimpleResponseDto> getBoards(AuthUser user) {
        isValidUserMemberRole(user);
        return boardRepository.findById(user.getUserId())
                .stream()
                .map(BoardSimpleResponseDto::from)
                .toList();
    }

    // 보드 단건 조회
    public BoardDetailResponseDto getBoard(AuthUser user, Long id) {
        isValidUserMemberRole(user);
        Board board = isValidBoard(id);
        List<Lists> lists = listsRepository.findAllByBoard(board);
        List<Card> card = cardRepository.findByBoardId(board.getId());
        return BoardDetailResponseDto.of(board, lists, card);
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(AuthUser user, Long boardId) {
        isValidUserMemberRole(user);
        isValidBoard(boardId);
        boardRepository.deleteById(boardId);
    }

    // 보드 수정
    @Transactional
    public BoardResponseDto updateBoard(AuthUser user, Long boardId, BoardUpdateRequestDto requestDto) {
        isValidUserMemberRole(user);
        Board board = isValidBoard(boardId);
        Workspace workspace = board.getWorkspace();
        board.updateBoard(workspace,requestDto.getTitle(),requestDto.getBackgroundColor(),requestDto.getImage());
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.from(savedBoard);
    }

    //     유저 이메일로 멤버 권한 확인
    public void isValidUserMemberRole(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_READ_ONLY));
        if (MemberRole.READONLY.equals(member.getMemberRole())) {
            throw new CustomException(ErrorCode.MEMBER_READ_ONLY);
        }
    }

    // 유효한 보드 인지 확인
    public Board isValidBoard(Long boardId) throws CustomException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return board;
    }

    // 유효한 워크스페이스 인지 확인
    public Workspace isValidWorkspace(Long workspaceId) throws CustomException {
        Member member = memberRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_READ_ONLY));
        Workspace workspace = workspaceRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));
        return workspace;
    }

}
