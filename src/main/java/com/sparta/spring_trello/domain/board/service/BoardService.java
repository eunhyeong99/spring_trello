package com.sparta.spring_trello.domain.board.service;

import com.sparta.spring_trello.domain.board.dto.BoardSimpleResponseDto;
import com.sparta.spring_trello.domain.board.dto.request.BoardRequestDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.spring_trello.domain.board.dto.response.BoardResponseDto;
import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.board.repository.BoardRepository;
import com.sparta.spring_trello.domain.list.entity.Lists;
import com.sparta.spring_trello.domain.list.repository.ListsRepository;
import com.sparta.spring_trello.exception.CustomException;
import com.sparta.spring_trello.exception.ErrorCode;
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
//    private final CardRepository cardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String backgroundColor, String image) {
        isValidWorkspace(boardRequestDto.getWorkspaceId());
        Board newboard = new Board(
                boardRequestDto.getTitle(),
                backgroundColor,
                image);
        Board savedBoard = boardRepository.save(newboard);
        return BoardResponseDto.from(newboard);
    }

    public List<BoardSimpleResponseDto> getBoards(String email) {
        return boardRepository.findAllByEmail(email)
                .stream()
                .map(BoardSimpleResponseDto::from)
                .toList();
    }

    public BoardDetailResponseDto getBoard(String email, Long boardId) {
        Board board = isValidBoard(boardId);
        List<Lists> lists = listsRepository.findAllByBoard(board);
//        List<Card> card = cardRepository.findByBoardId(board.getId());
        return BoardDetailResponseDto.of(board, lists);
    }

    @Transactional
    public void deleteBoard(String email, Long boardId) {
        isValidBoard(boardId);
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public BoardResponseDto updateBoard(String email, Long boardId, String title, String backgroundColor, String image) {
        Board board = isValidBoard(boardId);
        board.updateBoard(title, backgroundColor, image);
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.from(savedBoard);
    }

    public Board isValidBoard(Long boardId) throws CustomException {
        boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return null;
    }

    public void isValidWorkspace(Long workspaceId) throws CustomException {
        boardRepository.findById(workspaceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

}
