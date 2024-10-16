package com.sparta.spring_trello.domain.board.service;

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
import com.sparta.spring_trello.domain.list.entity.Lists;
import com.sparta.spring_trello.domain.list.repository.ListsRepository;
import com.sparta.spring_trello.domain.member.entity.Member;
import com.sparta.spring_trello.domain.member.entity.MemberRole;
import com.sparta.spring_trello.domain.member.repository.MemberRepository;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
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

    @Transactional
    public BoardResponseDto createBoard(String email, BoardRequestDto boardRequestDto, String backgroundColor, String image) {
        isValidUserMemberRole(email);
        isValidWorkspace(boardRequestDto.getWorkspaceId());
        Board newboard = new Board(
                boardRequestDto.getTitle(),
                backgroundColor,
                image);
        Board savedBoard = boardRepository.save(newboard);
        return BoardResponseDto.from(savedBoard);
    }

    public List<BoardSimpleResponseDto> getBoards(String email) {
        isValidUserMemberRole(email);
        return boardRepository.findAllByEmail(email)
                .stream()
                .map(BoardSimpleResponseDto::from)
                .toList();
    }

    public BoardDetailResponseDto getBoard(String email, Long boardId) {
        isValidUserMemberRole(email);
        Board board = isValidBoard(boardId);
        List<Lists> lists = listsRepository.findAllByBoard(board);
        List<Card> card = cardRepository.findByBoardId(board.getId());
        return BoardDetailResponseDto.of(board, lists, card);
    }

    @Transactional
    public void deleteBoard(String email, Long boardId) {
        isValidUserMemberRole(email);
        isValidBoard(boardId);
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public BoardResponseDto updateBoard(String email, Long boardId, String title, String backgroundColor, String image) {
        isValidUserMemberRole(email);
        Board board = isValidBoard(boardId);
        board.updateBoard(title, backgroundColor, image);
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.from(savedBoard);
    }

    public void isValidUserMemberRole(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Member member = memberRepository.findByUserId(user.getId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_READ_ONLY));
        if (MemberRole.READONLY.equals(member.getMemberRole())) {
            throw new CustomException(ErrorCode.MEMBER_READ_ONLY);
        }
    }

    public Board isValidBoard(Long boardId) throws CustomException {
        boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return null;
    }

    public void isValidWorkspace(Long workspaceId) throws CustomException {
        boardRepository.findById(workspaceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

}
