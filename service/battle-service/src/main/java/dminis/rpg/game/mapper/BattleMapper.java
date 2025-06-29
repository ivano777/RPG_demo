package dminis.rpg.game.mapper;

import dminis.rpg.game.dto.ActionDTO;
import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.dto.TurnDTO;
import dminis.rpg.game.hero.entity.Hero;
import dminis.rpg.game.entity.Action;
import dminis.rpg.game.entity.Battle;
import dminis.rpg.game.entity.CharacterSnapshot;
import dminis.rpg.game.entity.Turn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    @Mapping(source = "atk.level", target = "atk")
    @Mapping(source = "def.level", target = "def")
    @Mapping(source = "lck.level", target = "lck")
    CharacterSnapshot toSnap(Hero hero);

    BattleDTO toDTO(Battle source);
    TurnDTO toDTO(Turn source);
    ActionDTO toDTO(Action source);
}
