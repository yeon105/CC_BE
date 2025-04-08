package com.example.choiceculture.domain.festival.repository.querydsl;

import com.example.choiceculture.domain.festival.dto.FestivalInfoDTO;
import com.example.choiceculture.domain.festival.dto.FestivalRequestDTO;
import com.example.choiceculture.domain.festival.entity.FestivalInfo;
import com.example.choiceculture.domain.festival.enums.AccessState;
import com.example.choiceculture.util.file.AwsS3Util;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.choiceculture.domain.common.entity.QCommonInfo.commonInfo;
import static com.example.choiceculture.domain.festival.entity.QFestivalImage.festivalImage;
import static com.example.choiceculture.domain.festival.entity.QFestivalInfo.festivalInfo;
import static com.example.choiceculture.domain.festival.entity.QPlaceInfo.placeInfo;

@Slf4j
@RequiredArgsConstructor
public class FestivalInfoRepositoryImpl implements FestivalInfoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final AwsS3Util awsS3Util;

    @Override
    public FestivalInfoDTO findByFestivalId(Integer festivalId) {
        FestivalInfoDTO dto = jpaQueryFactory
                .select(Projections.fields(FestivalInfoDTO.class,
                        festivalInfo.id,
                        festivalInfo.festivalName,
                        festivalInfo.placeName,
                        placeInfo.placeName.as("placeDetailName"),
                        placeInfo.placeLocation,
                        festivalInfo.categoryId,
                        commonInfo.name.as("categoryName"),
                        festivalInfo.fromDate,
                        festivalInfo.toDate,
                        festivalInfo.festivalState,
                        festivalInfo.salePercent,
                        festivalInfo.festivalPrice,
                        festivalInfo.salePrice,
                        festivalInfo.runningTime,
                        festivalInfo.mdPick,
                        festivalInfo.premier,
                        festivalInfo.age,
                        festivalInfo.ranking,
                        festivalInfo.postImage,
                        festivalImage.imgSrc1,
                        festivalImage.imgSrc2,
                        festivalImage.imgSrc3))
                .from(festivalInfo)
                .leftJoin(commonInfo).on(festivalInfo.categoryId.eq(commonInfo.id))
                .leftJoin(placeInfo).on(festivalInfo.id.eq(placeInfo.festival.id))
                .leftJoin(festivalImage).on(festivalInfo.id.eq(festivalImage.festival.id))
                .fetchJoin()
                .where(festivalInfo.id.eq(festivalId))
                .fetchOne();

        if (dto == null) {
            throw new EntityNotFoundException("해당 공연이 없습니다.");
        }

        // S3 Presigned URL 생성 및 설정
        String postImageUrl = awsS3Util.getPresignedUrl(dto.getPostImage());
        dto.setPostImage(postImageUrl != null ? postImageUrl : dto.getPostImage());

        String imgSrc1Url = awsS3Util.getPresignedUrl(dto.getImgSrc1());
        dto.setImgSrc1(imgSrc1Url != null ? imgSrc1Url : dto.getImgSrc1());

        String imgSrc2Url = awsS3Util.getPresignedUrl(dto.getImgSrc2());
        dto.setImgSrc2(imgSrc2Url != null ? imgSrc2Url : dto.getImgSrc2());

        String imgSrc3Url = awsS3Util.getPresignedUrl(dto.getImgSrc3());
        dto.setImgSrc3(imgSrc3Url != null ? imgSrc3Url : dto.getImgSrc3());

        return dto;
    }

    @Override
    public List<FestivalInfo> findByDTO(FestivalRequestDTO requestDTO) {
        return jpaQueryFactory
                .selectFrom(festivalInfo)
                .where(
                        eqCategoryId(requestDTO.getCategoryId()),
                        eqMdPick(requestDTO.getMdPick()),
                        eqPremier(requestDTO.getPremier()),
                        festivalInfo.accessState.eq(AccessState.Y)
                )
                .fetch();
    }

    public List<FestivalInfoDTO> findByRanking() {
        return jpaQueryFactory.select(Projections.fields(FestivalInfoDTO.class,
                        festivalInfo.id,
                        festivalInfo.festivalName,
                        festivalInfo.placeName,
                        placeInfo.placeName.as("placeDetailName"),
                        festivalInfo.categoryId,
                        festivalInfo.fromDate,
                        festivalInfo.toDate,
                        festivalInfo.festivalState,
                        festivalInfo.runningTime,
                        festivalInfo.premier,
                        festivalInfo.ranking,
                        festivalInfo.postImage))
                .from(festivalInfo)
                .leftJoin(placeInfo).on(festivalInfo.id.eq(placeInfo.festival.id))
                .where(festivalInfo.accessState.eq(AccessState.Y))
                .orderBy(festivalInfo.ranking.intValue().asc())
                .fetch();
    }

    public List<FestivalInfo> findByDTOCategory(FestivalRequestDTO requestDTO) {
        return jpaQueryFactory
                .selectFrom(festivalInfo)
                .where(
                        festivalInfo.categoryId.like(requestDTO.getCategoryId() + "%"),
                        eqMdPick(requestDTO.getMdPick()),
                        eqPremier(requestDTO.getPremier()),
                        festivalInfo.accessState.eq(AccessState.Y)
                )
                .fetch();
    }

    private BooleanExpression eqCategoryId(String categoryId) {
        if (categoryId == null) {
            return null;
        }
        return festivalInfo.categoryId.eq(categoryId);
    }

    private BooleanExpression eqPremier(String premier) {
        if (premier == null) {
            return null;
        }
        return festivalInfo.premier.stringValue().eq(premier);
    }

    private BooleanExpression eqMdPick(String mdPick) {
        if (mdPick == null) {
            return null;
        }
        return festivalInfo.mdPick.stringValue().eq(mdPick);
    }
}
