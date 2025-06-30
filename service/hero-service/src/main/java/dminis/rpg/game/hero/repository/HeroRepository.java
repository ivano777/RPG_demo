package dminis.rpg.game.hero.repository;


import dminis.rpg.game.entity.hero.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    /* --- query di utilità comuni --- */

    /** tutti gli eroi vivi */
    List<Hero> findByStatus(Hero.LifeStatus status);

    /** ricerca case–insensitive per nome esatto */
    Optional<Hero> findByNameIgnoreCase(String name);

    /** esiste già qualcun altro con questo nome? (per validazione) */
    boolean existsByNameIgnoreCase(String name);
}
