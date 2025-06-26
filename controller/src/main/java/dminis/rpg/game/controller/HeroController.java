package dminis.rpg.game.controller;

import dminis.rpg.game.hero.dto.HeroDTO;
import dminis.rpg.game.hero.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public List<HeroDTO> list() {
        return heroService.findAll();
    }

    @GetMapping("/{id}")
    public HeroDTO get(@PathVariable Long id) {
        return heroService.findById(id);
    }

    @PostMapping
    public HeroDTO create(@RequestBody HeroDTO hero) {
        return heroService.save(hero);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        heroService.delete(id);
    }
}