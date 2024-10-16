package com.sparta.spring_trello.domain.list.repository;

import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.list.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ListsRepository extends JpaRepository<Lists, Long> {

    // 순서가 제일 큰 값 찾기
    @Query("SELECT MAX(l.order) FROM Lists l")
    Optional<Integer> findMaxOrder();

    // 순서 수정 시 자리 찾기
    List<Lists> findAllByOrderBetween(int start, int end);

    // 삭제하려는 리스트 보다 순서가 큰 경우 순서 -1 로 재부여
    List<Lists> findAllByIdAndOrderGreaterThan(Long listsId, Integer order);

    // 보드 아이디로 찾기
    List<Lists> findAllByBoard(Board board);
}
