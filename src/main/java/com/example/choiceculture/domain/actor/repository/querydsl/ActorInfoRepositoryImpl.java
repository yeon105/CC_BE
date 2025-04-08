package com.example.choiceculture.domain.actor.repository.querydsl;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.choiceculture.domain.actor.entity.QActorInfo.actorInfo;
import static com.example.choiceculture.domain.festival.entity.QFestivalInfo.festivalInfo;

@Slf4j
@RequiredArgsConstructor
public class ActorInfoRepositoryImpl implements ActorInfoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ActorResponseDTO> getActors(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by("festivalName").ascending()
                        .and(Sort.by("actorCharacter").ascending())
        );

        List<ActorResponseDTO> dtoList = jpaQueryFactory
                .select(Projections.fields(ActorResponseDTO.class,
                        festivalInfo.festivalName,
                        actorInfo.actorCharacter,
                        actorInfo.actorName,
                        actorInfo.id
                ))
                .from(actorInfo)
                .where(containsKeyword(requestDTO.getSearchTerm()))
                .leftJoin(festivalInfo).on(actorInfo.festivalId.eq(festivalInfo.id))
                .orderBy(festivalInfo.festivalName.asc(), actorInfo.actorCharacter.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ActorResponseDTO> countQuery = jpaQueryFactory
                .select(Projections.fields(ActorResponseDTO.class,
                        festivalInfo.festivalName,
                        actorInfo.actorCharacter,
                        actorInfo.actorName,
                        actorInfo.id
                ))
                .from(actorInfo)
                .where(containsKeyword(requestDTO.getSearchTerm()))
                .leftJoin(festivalInfo).on(actorInfo.festivalId.eq(festivalInfo.id));

        return PageableExecutionUtils.getPage(dtoList, pageable, countQuery::fetchCount);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return actorInfo.actorName.like("%" + keyword + "%")
                .or(actorInfo.actorCharacter.like("%" + keyword + "%")
                        .or(festivalInfo.festivalName.like("%" + keyword + "%")));
    }

}
