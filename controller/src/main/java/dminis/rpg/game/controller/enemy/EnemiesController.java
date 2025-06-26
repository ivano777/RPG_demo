package dminis.rpg.game.controller.enemy;

import dminis.rpg.game.enemy.dto.EnemyDTO;
import dminis.rpg.game.enemy.service.EnemyService;
import dminis.rpg.game.request.EnemyNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enemies")
@RequiredArgsConstructor
public class EnemiesController {

    private final EnemyService enemyService;

    @GetMapping
    public List<EnemyDTO> list() {
        return enemyService.findAll();
    }

    @PostMapping("/create")
    public EnemyDTO create(@RequestBody EnemyNameRequest name) {
        return enemyService.create(name.getName());
    }



}