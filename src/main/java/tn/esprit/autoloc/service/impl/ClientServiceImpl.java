package tn.esprit.autoloc.service.impl;


import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (client.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant");
        }
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }


    @Override
    public Client update(Long id, Client client) {
        // 1. Rechercher le client existant dans la base
        Client existant = findById(id);

        // 2. Mettre à jour ses attributs
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());

        // 3. Enregistrer les modifications
        return clientRepository.save(existant);
    }


    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }
}