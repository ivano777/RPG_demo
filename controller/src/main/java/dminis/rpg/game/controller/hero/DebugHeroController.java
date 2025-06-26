package dminis.rpg.game.controller.hero;

import dminis.rpg.game.hero.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Profile({"dev", "debug"})
@RestController
@RequestMapping("debug/hero")
@RequiredArgsConstructor
public class DebugHeroController {

    private final HeroService service;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/reset-heroes")
    public void resetHero() {
        service.deleteAll();
    }
}
