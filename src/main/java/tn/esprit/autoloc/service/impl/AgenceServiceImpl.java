
package tn.esprit.autoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO create(AgenceDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant"
            );
        }

        Agence agence = agenceMapper.toEntity(dto);

        Agence agenceSaved = agenceRepository.save(agence);

        return agenceMapper.toDto(agenceSaved);
    }

    @Override
    public AgenceDTO findById(Long id) {

        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));

        return agenceMapper.toDto(agence);
    }

    @Override
    public List<AgenceDTO> findAll() {

        return agenceRepository.findAll()
                .stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    public long count() {
        return agenceRepository.count();
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO dto) {

        Agence existante = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));

        existante.setNom(dto.getNom());
        existante.setVille(dto.getVille());
        existante.setAdresse(dto.getAdresse());
        existante.setTelephone(dto.getTelephone());

        Agence agenceUpdated = agenceRepository.save(existante);

        return agenceMapper.toDto(agenceUpdated);
    }

    @Override
    public void deleteById(Long id) {

        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }

        agenceRepository.deleteById(id);
    }
}
