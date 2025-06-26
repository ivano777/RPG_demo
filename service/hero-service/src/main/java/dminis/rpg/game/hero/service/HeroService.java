package dminis.rpg.game.hero.service;

import dminis.rpg.game.hero.dto.HeroDTO;
import dminis.rpg.game.hero.mapper.HeroMapper;
import dminis.rpg.game.hero.repository.HeroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeroService {
    private final HeroRepository repository;
    private final HeroMapper mapper;

    public HeroDTO findById(Long id){
        return repository.findById(id).map(mapper::toDto).orElseThrow(EntityNotFoundException::new);
    }

    public List<HeroDTO> findAll(){
        var entities = repository.findAll();
        return mapper.toDtoList(entities);
    }

    public HeroDTO save(HeroDTO dto){
        var entity = mapper.toEntity(dto);
        var res = repository.save(entity);
        return mapper.toDto(res);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

}
