package tn.esprit.autoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    @Transactional
    public VehiculeDTO create(VehiculeDTO dto) {

        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }

        Vehicule vehicule = vehiculeMapper.toEntity(dto);

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Agence", dto.getAgenceId()));
            vehicule.setAgence(agence);
        }

        Vehicule saved = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", id));

        return vehiculeMapper.toDto(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {

        return vehiculeRepository.findAll()
                .stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public VehiculeDTO update(Long id, VehiculeDTO dto) {

        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", id));

        existant.setImmatriculation(dto.getImmatriculation());
        existant.setMarque(dto.getMarque());
        existant.setModele(dto.getModele());

        // Conversion String vers les enums via MapStruct
        Vehicule donnees = vehiculeMapper.toEntity(dto);
        existant.setCategorie(donnees.getCategorie());
        existant.setTarifJournalier(dto.getTarifJournalier());
        existant.setStatut(donnees.getStatut());

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Agence", dto.getAgenceId()));
            existant.setAgence(agence);
        } else {
            existant.setAgence(null);
        }

        // Les équipements existants sont conservés :
        // ils ne font pas partie de VehiculeDTO.

        Vehicule updated = vehiculeRepository.save(existant);

        return vehiculeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }

        vehiculeRepository.deleteById(id);
    }

    // Affecter un véhicule à une agence
    @Override
    @Transactional
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {

        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", vehiculeId));

        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", agenceId));

        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }

    // Désaffecter un véhicule de son agence
    @Override
    @Transactional
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {

        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", vehiculeId));

        if (vehicule.getAgence() == null) {
            throw new IllegalStateException(
                    "Ce véhicule n'est actuellement affecté à aucune agence");
        }

        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }
}