package dminis.rpg.game.service;

import dminis.rpg.game.dto.RewardDTO;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.hero.Hero;
import dminis.rpg.game.mapper.BattleMapper;
import dminis.rpg.game.utility.BattleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RewardApplier {
    private final BattleMapper mapper;

    @Transactional
    public RewardDTO applyReward(Battle battle) {
        if (Hero.LifeStatus.DEAD.equals(battle.getHero().getStatus())) {
            throw new IllegalStateException("Un eroe morto non può riscuotere una ricompensa.");
        }

        BattleUtils.handleExpPack(battle.getExpPack(), battle.getHero());
        var res = new RewardDTO();
        res.setHero(mapper.toSnapDTO(battle.getHero()));
        res.setExpPack(mapper.toDTO(battle.getExpPack()));
        return res;
    }

}
