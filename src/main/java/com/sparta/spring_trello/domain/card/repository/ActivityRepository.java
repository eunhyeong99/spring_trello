package com.sparta.spring_trello.domain.card.repository;

import com.sparta.spring_trello.domain.card.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCardId(Long cardId);
}
