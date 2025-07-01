package dminis.rpg.game.mapper;

import dminis.rpg.game.dto.*;
import dminis.rpg.game.entity.battle.*;
import dminis.rpg.game.entity.hero.Hero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    @Mapping(source = "atk.level", target = "atk")
    @Mapping(source = "def.level", target = "def")
    @Mapping(source = "lck.level", target = "lck")
    CharacterSnapshot toSnap(Hero hero);

    @Mapping(source = "atk.level", target = "atk")
    @Mapping(source = "def.level", target = "def")
    @Mapping(source = "lck.level", target = "lck")
    CharacterSnapshotDTO toSnapDTO(Hero hero);

    @Mappings({
            @Mapping(source = "hero.id", target = "heroId"),
            @Mapping(source = "turns", target = "turns"),
            @Mapping(target = "turns[].battle", ignore = true)
    })
    BattleDTO toDTO(Battle battle);

    List<BattleDTO> toDTOList (List<Battle> battle);

    @Mappings({
            @Mapping(source = "battle", target = "battle"),
            @Mapping(target = "battle.turns", ignore = true)
    })    TurnDTO toDTO(Turn source);
    ActionDTO toDTO(Action source);

    CharacterSnapshotDTO toDTO(CharacterSnapshot source);

    ExpPackDTO toDTO(ExpPack source);
}
