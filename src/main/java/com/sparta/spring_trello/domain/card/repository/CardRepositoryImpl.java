package com.sparta.spring_trello.domain.card.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spring_trello.domain.card.dto.request.CardSearchDTO;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.entity.QCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CardRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Card> searchCards(CardSearchDTO criteria, Pageable pageable) {
        QCard qCard = QCard.card;
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
            builder.and(qCard.title.containsIgnoreCase(criteria.getTitle()));
        }
        if (criteria.getContents() != null && !criteria.getContents().isEmpty()) {
            builder.and(qCard.contents.containsIgnoreCase(criteria.getContents()));
        }
        if (criteria.getDeadline() != null) {
            builder.and(qCard.deadline.eq(criteria.getDeadline()));
        }
        if (criteria.getUserId() != null) {
            builder.and(qCard.userId.eq(criteria.getUserId()));
        }
        if (criteria.getBoardId() != null) {
            builder.and(qCard.board.id.eq(criteria.getBoardId()));
        }

        // 페이징 없이 전체 결과 조회 (4번)
        List<Card> allCards = queryFactory.selectFrom(qCard)
                .where(builder)
                .fetch();

        // 결과 리스트 가져오기
        List<Card> cards = queryFactory.selectFrom(qCard)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory.select(qCard.count())
                .from(qCard)
                .where(builder)
                .fetchOne();

        // fetchOne() 결과가 null일 수 있으므로 안전하게 처리
        long total = (countResult != null) ? countResult : 0L;

        return new PageImpl<>(cards, pageable, total);
    }
}