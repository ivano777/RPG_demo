package dminis.rpg.game.enemy.service;

import dminis.rpg.game.enemy.dto.EnemyDTO;
import dminis.rpg.game.enemy.entity.Enemy;
import dminis.rpg.game.enemy.mapper.EnemyMapper;
import dminis.rpg.game.enemy.repository.EnemyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnemyService {
    private final EnemyRepository repository;
    private final EnemyMapper mapper;

    public EnemyDTO create(String name){
     var entity = new Enemy();
     entity.setName(name);
     var res = repository.save(entity);
     return mapper.toDto(entity);
    }

    public EnemyDTO findByName(String name){
        return repository.findByNameIgnoreCase(name)
                .map(mapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<EnemyDTO> findAll(){
        var entity = repository.findAll();
        return mapper.toDtoList(entity);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
