package tn.esprit.autoloc.service.impl;



import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.IPaiementService;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    @Autowired
    private IPaiementRepository paiementRepository;

    @Override
    public Paiement create(Paiement paiement) {
        if (paiement.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau paiement ne doit pas avoir d'identifiant");
        }
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement update(Long id, Paiement paiement) {
        Paiement existant = findById(id);

        // Ajouter ici les attributs modifiables du paiement.
        // Ne pas modifier l'identifiant ni les relations sans nécessité.

        return paiementRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paiement", id);
        }
        paiementRepository.deleteById(id);
    }
}
