package com.example.choiceculture.domain.festival.service;

import com.example.choiceculture.domain.actor.dto.ActorInfoDTO;
import com.example.choiceculture.domain.actor.entity.ActorInfo;
import com.example.choiceculture.domain.actor.repository.ActorInfoRepository;
import com.example.choiceculture.domain.actor.service.ActorInfoService;
import com.example.choiceculture.domain.festival.dto.*;
import com.example.choiceculture.domain.festival.entity.*;
import com.example.choiceculture.domain.festival.enums.AccessState;
import com.example.choiceculture.domain.festival.enums.FestivalState;
import com.example.choiceculture.domain.festival.repository.*;
import com.example.choiceculture.domain.member.entity.Member;
import com.example.choiceculture.domain.member.repository.MemberRepository;
import com.example.choiceculture.util.file.AwsS3Util;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FestivalInfoServiceImpl implements FestivalInfoService {
    private final FestivalInfoRepository festivalInfoRepository;
    private final ActorInfoRepository actorInfoRepository;
    private final ActorInfoService actorInfoService;
    private final MemberRepository memberRepository;
    private final FestivalTimeRepository festivalTimeRepository;
    private final FestivalImageRepository festivalImageRepository;
    private final PlaceInfoRepository placeInfoRepository;
    private final FestivalInfoAccessRepository festivalInfoAccessRepository;
    private final AwsS3Util awsS3Util;

    @Override
    public List<FestivalInfoDTO> openTicket() {
        List<FestivalInfo> infoList = festivalInfoRepository.findByFromDate();
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("오픈예정 공연이 없습니다.");
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }

    @Override
    public FestivalInfoDTO getOne(Integer festivalId) {
        FestivalInfoDTO dto  = festivalInfoRepository.findByFestivalId(festivalId);
        if (dto  == null) {
            throw new EntityNotFoundException("해당 공연이 없습니다.");
        }
        return dto ;
    }

    @Override
    public List<FestivalInfoDTO> list(FestivalRequestDTO requestDTO) {
        List<FestivalInfo> infoList = festivalInfoRepository.findByDTO(requestDTO);
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 공연 목록이 없습니다.");
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }

    @Override
    public List<FestivalInfoDTO> listCategory(FestivalRequestDTO requestDTO) {
        List<FestivalInfo> infoList = festivalInfoRepository.findByDTOCategory(requestDTO);
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 공연 목록이 없습니다.");
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SearchResponseDTO search(String searchKeyword) {
        List<FestivalInfo> festivalInfoList = festivalInfoRepository.findBySearchKeyword(searchKeyword);
        List<FestivalInfoDTO> festivalDTOList = festivalInfoList.stream().map(this::entityToDTO).toList();

        List<ActorInfo> actorInfoList = actorInfoRepository.findBySearchKeyword(searchKeyword);
        List<ActorInfoDTO> actorDTOList = actorInfoList.stream().map(actorInfoService::entityToDTO).toList();

        if (festivalDTOList.isEmpty() && actorDTOList.isEmpty()) {
            throw new EntityNotFoundException(searchKeyword + "에 대한 공연명/등장인물/장소가 없습니다.");
        }

        return new SearchResponseDTO(festivalDTOList, actorDTOList);
    }

    @Override
    public List<FestivalInfoDTO> rankingList() {
        List<FestivalInfoDTO> infoList = festivalInfoRepository.findByRanking();
        return infoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FestivalInfoDTO> favoriteRanking(String userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        List<FestivalInfo> infoList = festivalInfoRepository.findRankingByUserId(member.getUserFavorite1());
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 장르에 대한 공연이 없습니다.");
        }

        List<FestivalInfoDTO> dtoList = infoList.stream().map(this::entityToDTO).toList();
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FestivalInfoDTO> LimitRanking(int limit) {
        List<FestivalInfo> infoList = festivalInfoRepository.findRankingLimit(limit);
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 장르에 대한 공연이 없습니다.");
        }

        List<FestivalInfoDTO> dtoList = infoList.stream().map(this::entityToDTO).toList();
        return dtoList;
    }

    @Override
    public List<FestivalInfoDTO> favoriteLimit(String userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        List<FestivalInfo> infoList = festivalInfoRepository.findByFavorite(member.getUserFavorite1());
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 장르는 공연이 없습니다.");
        }

        return infoList.stream().map(this::entityToDTO).toList();
    }

    FestivalState nowState(LocalDate fromDate, LocalDate toDate) {
        FestivalState state;

        LocalDate today = LocalDate.now();

        if (toDate.isBefore(today)) {
            state = FestivalState.END;
        } else if (fromDate.isAfter(today)) {
            state = FestivalState.YET;
        } else {
            state = FestivalState.NOW;
        }

        return state;
    }

    void time(Integer festivalId) {
        List<FestivalTime> timeList = festivalTimeRepository.findByFestivalId(festivalId);
        if (!timeList.isEmpty()) {
            timeList.forEach(time -> {
                festivalTimeRepository.deleteByFestivalId(time.getFestivalInfo().getId());
            });
        }
    }

    @Transactional
    @Override
    public void addFestival(FestivalAddDTO addDTO) {
        FestivalInfo info = (addDTO.getFestivalId() != null)
                ? festivalInfoRepository.findById(addDTO.getFestivalId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."))
                : FestivalInfo.builder()
                .regDate(LocalDateTime.now())
                .ranking(festivalInfoRepository.festivalCount() + 1)
                .build();

        log.info("addDTO: {}", addDTO);

        info.setFestivalName(addDTO.getFestivalName());
        info.setPlaceName(addDTO.getPlaceId());
        info.setCategoryId(addDTO.getCategoryId());
        info.setFromDate(addDTO.getFromDate());
        info.setToDate(addDTO.getToDate());
        info.setSalePercent(addDTO.getSalePercent());
        info.setFestivalPrice(addDTO.getFestivalPrice());
        info.setSalePrice(Math.max(0, Math.round(addDTO.getFestivalPrice() * (1 - (float) (addDTO.getSalePercent() != null ? addDTO.getSalePercent() : 0) / 100))));
        info.setRunningTime(addDTO.getRunningTime());
        info.setAge(addDTO.getAge());
        info.setUpDate(LocalDateTime.now());

        if (addDTO.getPostImage() != null && !addDTO.getPostImage().isEmpty()) {
            String s3PostImageUrl = awsS3Util.uploadFile(addDTO.getPostImage());
            info.setPostImage(s3PostImageUrl);
        }

        FestivalState state = nowState(info.getFromDate(), info.getToDate());
        info.setFestivalState(state);

        FestivalInfo saved = festivalInfoRepository.save(info);

        time(info.getId());

        addDTO.getTimeDTOS().forEach(time -> {
            LocalDate startDate = saved.getFromDate();
            LocalDate endDate = saved.getToDate();
            LocalDate maxEndDate = startDate.plusMonths(1).minusDays(1);

            if (endDate.isAfter(maxEndDate)) {
                endDate = maxEndDate;
            }

            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                boolean shouldSave = switch (time.getHolidayType()) {
                    case "HDO1" -> true;
                    case "HDO2" -> isWeekday(currentDate);
                    case "HDO3" -> isSaturday(currentDate);
                    case "HDO4" -> isSunday(currentDate);
                    default -> {
                        log.info("holiday type error: {}", time.getHolidayType());
                        yield false;
                    }
                };

                if (shouldSave) {
                    FestivalTime festivalTime = FestivalTime.builder()
                            .holidayType(time.getHolidayType())
                            .date(currentDate)
                            .time(time.getTime())
                            .festivalInfo(saved)
                            .build();

                    festivalTimeRepository.save(festivalTime);
                }

                currentDate = currentDate.plusDays(1);
            }
        });

        FestivalImage imgByFestivalId = festivalImageRepository.findByFestivalId(saved.getId());
        if (imgByFestivalId != null) {
            festivalImageRepository.deleteById(imgByFestivalId.getId());
        }

        FestivalImage.FestivalImageBuilder imgBuilder = FestivalImage.builder().festival(info);

        if (addDTO.getImgSrc1() != null && !addDTO.getImgSrc1().isEmpty()) {
            imgBuilder.imgSrc1(awsS3Util.uploadFile(addDTO.getImgSrc1()));
        }
        if (addDTO.getImgSrc2() != null && !addDTO.getImgSrc2().isEmpty()) {
            imgBuilder.imgSrc2(awsS3Util.uploadFile(addDTO.getImgSrc2()));
        }
        if (addDTO.getImgSrc3() != null && !addDTO.getImgSrc3().isEmpty()) {
            imgBuilder.imgSrc3(awsS3Util.uploadFile(addDTO.getImgSrc3()));
        }

        festivalImageRepository.save(imgBuilder.build());

        PlaceInfo placeByFestivalId = placeInfoRepository.findByFestivalId(saved.getId());
        if (placeByFestivalId != null) {
            placeInfoRepository.deleteById(placeByFestivalId.getId());
        }

        PlaceInfo place = PlaceInfo.builder()
                .festival(saved)
                .placeName(addDTO.getPlaceName())
                .placeLocation(addDTO.getPlaceLocation())
                .build();

        placeInfoRepository.save(place);

        FestivalInfoAccess infoAccess = festivalInfoAccessRepository.findByFestivalId(saved.getId());

        if (saved.getAccessState().equals(AccessState.N) && infoAccess == null) {
            FestivalInfoAccess access = FestivalInfoAccess.builder()
                    .festivalMediaLink(addDTO.getMediaLink())
                    .festival(saved)
                    .build();
        }

    }

    private boolean isWeekday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    private boolean isSaturday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    private boolean isSunday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

}
