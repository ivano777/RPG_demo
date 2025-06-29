package dminis.rpg.game.controller.battle;

import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.request.HeroIdRequest;
import dminis.rpg.game.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService service;

    @PostMapping("/resume-start")
    BattleDTO startBattle(@RequestBody HeroIdRequest heroId){
       return service.resumeStartBattle(heroId.getId());
    }
}
