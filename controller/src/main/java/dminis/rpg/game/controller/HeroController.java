package dminis.rpg.game.controller;

import dminis.rpg.game.hero.dto.HeroDTO;
import dminis.rpg.game.hero.service.HeroService;
import dminis.rpg.game.request.HeroNameRequest;
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

    @PostMapping("/create")
    public HeroDTO create(@RequestBody HeroNameRequest name) {
        return heroService.create(name.getName());
    }

    //todo spostare in debug
    @PostMapping
    public HeroDTO save(@RequestBody HeroDTO hero) {
        return heroService.save(hero);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        heroService.delete(id);
    }
}