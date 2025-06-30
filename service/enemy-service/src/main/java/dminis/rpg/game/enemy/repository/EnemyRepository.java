package dminis.rpg.game.enemy.repository;

import dminis.rpg.game.entity.enemy.Enemy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public interface EnemyRepository extends JpaRepository<Enemy, Long> {
    Optional<Enemy> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    default Optional<Enemy> findRandom() {
        long total = count();
        if (total == 0) return Optional.empty();
        int idx = ThreadLocalRandom.current().nextInt((int) total);
        return findAll(PageRequest.of(idx, 1)).stream().findFirst();
    }
}
