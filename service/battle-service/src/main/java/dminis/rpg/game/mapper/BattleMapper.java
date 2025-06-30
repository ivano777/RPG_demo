package dminis.rpg.game.mapper;

import dminis.rpg.game.dto.ActionDTO;
import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.dto.TurnDTO;
import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.CharacterSnapshot;
import dminis.rpg.game.entity.battle.Turn;
import dminis.rpg.game.entity.hero.Hero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    @Mapping(source = "atk.level", target = "atk")
    @Mapping(source = "def.level", target = "def")
    @Mapping(source = "lck.level", target = "lck")
    CharacterSnapshot toSnap(Hero hero);

    BattleDTO toDTO(Battle source);

    @Mapping(source = "battle.id", target = "battleId")
    TurnDTO toDTO(Turn source);
    ActionDTO toDTO(Action source);
}
