package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.dto.SimpleMatchDTO;
import com.pmolinav.predictionslib.model.Match;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    Match matchDtoToEntity(MatchDTO matchDTO);

    @Mapping(target = "h2hOdds", ignore = true)
        // Manually handled.
    MatchDTO matchEntityToDto(Match match);

    SimpleMatchDTO matchEntityToSimpleDto(Match match);

    SimpleMatchDTO matchDTOToSimpleDto(MatchDTO matchDTO);

    void updateEntityFromDto(MatchDTO dto, @MappingTarget Match entity);

    // Personalized logic to get h2h Odds.
    @AfterMapping
    default void filterH2HOdds(Match match, @MappingTarget MatchDTO dto) {
        if (match == null || match.getOdds() == null) {
            return;
        }

        List<OddsDTO> filtered = match.getOdds().stream()
                .filter(o -> o.getEventType() != null)
                .filter(o -> o.getEventType().equalsIgnoreCase("H2H")
                        || o.getEventType().equalsIgnoreCase("h2h_lay"))
                .map(odds -> new OddsDTO(odds.getEventType(), odds.getMatchId(), odds.getLabel(),
                        odds.getValue(), odds.getPoint(), odds.getProvider(), odds.getActive()))
                .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            dto.setH2hOdds(filtered);
        }
    }
}