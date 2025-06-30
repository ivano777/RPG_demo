package dminis.rpg.game.hero.mapper;

import dminis.rpg.game.entity.hero.Hero;
import dminis.rpg.game.entity.hero.StatProgress;
import dminis.rpg.game.hero.dto.HeroDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeroMapper {

    HeroDTO toDto(Hero hero);

    List<HeroDTO> toDtoList(List<Hero> heroes);

    @InheritInverseConfiguration
    Hero toEntity(HeroDTO dto);

    HeroDTO.StatDto toStatDto(StatProgress stat);

    StatProgress toStatEntity(HeroDTO.StatDto dto);
}
