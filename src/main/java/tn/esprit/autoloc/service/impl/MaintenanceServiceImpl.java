package tn.esprit.autoloc.service.impl;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.service.IMaintenanceService;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        if (maintenance.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }


    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        // Récupérer la maintenance existante
        Maintenance existante = findById(id);

        // Modifier les attributs
        existante.setDateDebut(maintenance.getDateDebut());
        existante.setDateFin(maintenance.getDateFin());
        existante.setDescription(maintenance.getDescription());
        existante.setVehicule(maintenance.getVehicule());

        // Enregistrer les modifications
        return maintenanceRepository.save(existante);
    }


    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }
}
