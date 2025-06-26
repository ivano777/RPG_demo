package dminis.rpg.game.enemy.mapper;

import dminis.rpg.game.enemy.dto.EnemyDTO;
import dminis.rpg.game.enemy.entity.Enemy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnemyMapper {
    EnemyDTO toDto(Enemy source);

    List<EnemyDTO> toDtoList(List<Enemy> source);

    @InheritInverseConfiguration
    Enemy toEntity(EnemyDTO source);


}
