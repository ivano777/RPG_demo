package dminis.rpg.game.controller.enemy;

import dminis.rpg.game.enemy.service.EnemyService;
import dminis.rpg.game.hero.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Profile({"dev", "debug"})
@RestController
@RequestMapping("debug/enemy")
@RequiredArgsConstructor
public class DebugEnemyController {

    private final EnemyService service;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/reset-enemies")
    public void resetHero() {
        service.deleteAll();
    }
}
