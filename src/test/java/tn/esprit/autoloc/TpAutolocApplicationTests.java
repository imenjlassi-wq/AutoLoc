package tn.esprit.autoloc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    // PARTIE 2 : TEST AGENCE
    @Test
    void testAgenceRepository() {

        System.out.println("==== AGENCE ====");

        // 1. Créer une agence
        Agence agence = new Agence();
        agence.setNom("Agence1");
        agence.setVille("Tunis");
        agence.setAdresse("Tunis");
        agence.setTelephone("70111222");

        // 2. Enregistrer l'agence
        Agence agenceSaved = agenceRepository.save(agence);

        assertNotNull(agenceSaved);
        assertNotNull(agenceSaved.getId());

        System.out.println("Agence créée : " + agenceSaved);

        // 3. Afficher toutes les agences
        System.out.println("Toutes les agences : "
                + agenceRepository.findAll());

        // 4. Rechercher par ID
        Long id = agenceSaved.getId();

        assertTrue(agenceRepository.findById(id).isPresent());

        System.out.println("Recherche par ID : "
                + agenceRepository.findById(id));

        // 5. Vérifier l'existence
        assertTrue(agenceRepository.existsById(id));

        // 6. Vérifier le nombre
        assertTrue(agenceRepository.count() > 0);

        System.out.println("Nombre d'agences : "
                + agenceRepository.count());

        // 7. Supprimer l'agence
        agenceRepository.deleteById(id);

        assertFalse(agenceRepository.existsById(id));

        System.out.println("Suppression vérifiée.");
    }

    // PARTIE 3 : TEST CLIENT
    @Test
    void testClientRepository() {

        System.out.println("==== CLIENT ====");

        // 1. Créer deux clients
        Client client1 = new Client();
        client1.setNom("Ben Ali");
        client1.setPrenom("Sami");
        client1.setEmail("sami@example.com");
        client1.setTelephone("22111222");
        client1.setNumPermis("PERMIS001");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Amira");
        client2.setEmail("amira@example.com");
        client2.setTelephone("55111222");
        client2.setNumPermis("PERMIS002");
        client2.setDateInscription(LocalDate.now());

        // 2. Enregistrer les clients
        Client saved1 = clientRepository.save(client1);
        Client saved2 = clientRepository.save(client2);

        assertNotNull(saved1);
        assertNotNull(saved2);
        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());

        System.out.println("Client 1 : " + saved1);
        System.out.println("Client 2 : " + saved2);

        // 3. Afficher tous les clients
        System.out.println("Tous les clients : "
                + clientRepository.findAll());

        // 4. Rechercher le premier client
        Long id = saved1.getId();

        assertTrue(clientRepository.findById(id).isPresent());

        System.out.println("Client recherché : "
                + clientRepository.findById(id));

        // 5. Vérifier l'existence
        assertTrue(clientRepository.existsById(id));

        // 6. Vérifier un ID inexistant
        assertFalse(clientRepository.existsById(Long.MAX_VALUE));

        // 7. Vérifier le nombre de clients
        assertTrue(clientRepository.count() >= 2);

        System.out.println("Nombre de clients : "
                + clientRepository.count());
    }

    // PARTIE 4 : TEST VEHICULE
    @Test
    void testVehiculeRepository() {

        System.out.println("==== VEHICULE ====");

        // 1. Créer un véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123 TUN 456");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("120.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // 2. Enregistrer le véhicule
        Vehicule saved = vehiculeRepository.save(vehicule);

        assertNotNull(saved);
        assertNotNull(saved.getIdVehicule());

        Long id = saved.getIdVehicule();

        System.out.println("Véhicule créé : " + saved);

        // 3. Afficher tous les véhicules
        System.out.println("Tous les véhicules : "
                + vehiculeRepository.findAll());

        assertTrue(vehiculeRepository.findById(id).isPresent());

        // 4. Vérifier le nombre de véhicules
        assertTrue(vehiculeRepository.count() > 0);

        System.out.println("Nombre de véhicules : "
                + vehiculeRepository.count());

        // 5. Modifier le tarif
        saved.setTarifJournalier(new BigDecimal("150.00"));

        Vehicule updated = vehiculeRepository.save(saved);

        assertEquals(
                new BigDecimal("150.00"),
                updated.getTarifJournalier()
        );

        System.out.println("Véhicule modifié : " + updated);

        // 6. Supprimer le véhicule
        vehiculeRepository.deleteById(id);

        assertFalse(vehiculeRepository.existsById(id));

        System.out.println("Suppression vérifiée.");
    }

    // TEST DU CONTEXTE SPRING
    @Test
    void contextLoads() {
        assertNotNull(agenceRepository);
        assertNotNull(clientRepository);
        assertNotNull(vehiculeRepository);
    }
}