
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private IClientService clientService;

    @Test
    void testClientService() {

        // 1. CREATE : créer un client
        Client client = new Client();

        client.setNom("Jlassi");
        client.setPrenom("Imen");
        client.setEmail("jlassiimen1310@gmail.com");
        client.setTelephone("92252456");
        client.setNumPermis("PERMIS12345");
        client.setDateInscription(LocalDate.now());

        Client clientEnregistre = clientService.create(client);

        assertNotNull(clientEnregistre);
        assertNotNull(clientEnregistre.getId());

        Long id = clientEnregistre.getId();

        System.out.println("===== CREATE =====");
        System.out.println(clientEnregistre);

        // 2. READ ALL : afficher tous les clients
        System.out.println("===== FIND ALL =====");
        clientService.findAll().forEach(System.out::println);

        assertTrue(
                clientService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        // 3. FIND BY ID : rechercher le client
        System.out.println("===== FIND BY ID =====");

        Client clientRecupere = clientService.findById(id);

        assertNotNull(clientRecupere);
        assertEquals(id, clientRecupere.getId());
        assertEquals("Jlassi", clientRecupere.getNom());
        assertEquals("PERMIS12345", clientRecupere.getNumPermis());

        System.out.println(clientRecupere);
/*
        // 4. DELETE : supprimer le client
        System.out.println("===== DELETE =====");

        clientService.deleteById(id);

        System.out.println("Client supprimé : " + id);

        // 5. Vérifier que le client n'existe plus
        assertFalse(
                clientService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        System.out.println(
                "Vérification : le client a été supprimé avec succès."
        );*/
    }
}
