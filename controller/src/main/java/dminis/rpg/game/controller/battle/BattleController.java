package dminis.rpg.game.controller.battle;

import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.dto.RewardDTO;
import dminis.rpg.game.dto.TurnDTO;
import dminis.rpg.game.request.HeroIdRequest;
import dminis.rpg.game.request.PlayTurnRequest;
import dminis.rpg.game.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService service;

    @PostMapping("/resume-start")
    BattleDTO startBattle(@RequestBody HeroIdRequest heroId) {
        return service.resumeStartBattle(heroId.getId());
    }

    @PostMapping("/{battleId}/turn")
    public TurnDTO playTurn(@PathVariable("battleId") long battleId,
                            @RequestBody PlayTurnRequest req) {
        return service.playTurn(battleId, req.getActionType(), req.getActor());
    }

    @PutMapping("/{battleId}/reward")
    public RewardDTO applyReward(@PathVariable("battleId") long battleId) {
        return service.applyReward(battleId);
    }

    @PostMapping("/rewards")
    public void applyRewards() {
        service.applyAllRewards();
    }

    @GetMapping
    public List<BattleDTO> getAllBattles(){
        return service.getAllBattles();
    }

}
