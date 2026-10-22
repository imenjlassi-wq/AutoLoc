package tn.esprit.autoloc.service.impl;


import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    @Autowired
    private IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        if (employe.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }


    @Override
    public Employe update(Long id, Employe employe) {
        // 1. Rechercher l'employé existant
        Employe existant = findById(id);

        // 2. Modifier les attributs
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(employe.getAgence());

        // 3. Enregistrer les modifications
        return employeRepository.save(existant);
    }


    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
}
