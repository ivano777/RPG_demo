package dminis.rpg.game.repository;

import dminis.rpg.game.entity.battle.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends JpaRepository<Battle, Long> {
    Optional<Battle> findFirstByActiveTrueAndHeroId(Long aLong);
    Optional<Battle> findByIdAndActiveFalseAndExpPack_TakenFalse(Long id);
    List<Battle> findByActiveFalseAndExpPack_TakenFalse(); //todo per uno scheduler

}
