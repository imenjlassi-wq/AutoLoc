
package tn.esprit.autoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final IContratRepository contratRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public PaiementDTO create(PaiementDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau paiement ne doit pas avoir d'identifiant");
        }

        if (dto.getContratId() == null) {
            throw new IllegalArgumentException(
                    "Un paiement doit être associé à un contrat");
        }

        Contrat contrat = contratRepository.findById(dto.getContratId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", dto.getContratId()));

        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setContrat(contrat);

        Paiement saved = paiementRepository.save(paiement);

        return paiementMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDTO findById(Long id) {

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paiement", id));

        return paiementMapper.toDto(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAll() {

        return paiementRepository.findAll()
                .stream()
                .map(paiementMapper::toDto)
                .toList();
    }

    @Override
    public PaiementDTO update(Long id, PaiementDTO dto) {

        Paiement existant = paiementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paiement", id));

        existant.setMontant(dto.getMontant());
        existant.setDatePaiement(dto.getDatePaiement());
        existant.setModePaiement(
                paiementMapper.toEntity(dto).getModePaiement()
        );

        if (dto.getContratId() != null) {
            Contrat contrat = contratRepository.findById(dto.getContratId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Contrat", dto.getContratId()));

            existant.setContrat(contrat);
        }

        Paiement updated = paiementRepository.save(existant);

        return paiementMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {

        if (!paiementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paiement", id);
        }

        paiementRepository.deleteById(id);
    }
}
