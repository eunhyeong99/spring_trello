package com.sparta.spring_trello.domain.board.repository;

import com.sparta.spring_trello.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
