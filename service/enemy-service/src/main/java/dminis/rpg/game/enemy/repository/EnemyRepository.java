package dminis.rpg.game.enemy.repository;

import dminis.rpg.game.enemy.entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnemyRepository extends JpaRepository<Enemy, Long> {
    Optional<Enemy> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
