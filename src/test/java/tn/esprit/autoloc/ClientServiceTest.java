
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private IClientService clientService;

    @Test
    void testClientService() {

        // 1. CREATE
        ClientDTO client = new ClientDTO();

        client.setNom("Jlassi");
        client.setPrenom("Imen");
        client.setEmail("imen.test." + System.currentTimeMillis() + "@gmail.com");
        client.setTelephone("92252456");
        client.setNumPermis("PERMIS12345");
        client.setDateInscription(LocalDate.now());

        ClientDTO clientEnregistre = clientService.create(client);

        assertNotNull(clientEnregistre);
        assertNotNull(clientEnregistre.getId());

        Long id = clientEnregistre.getId();

        System.out.println("===== CREATE =====");
        System.out.println(clientEnregistre);

        // 2. FIND ALL
        System.out.println("===== FIND ALL =====");

        assertTrue(
                clientService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        // 3. FIND BY ID
        System.out.println("===== FIND BY ID =====");

        ClientDTO clientRecupere = clientService.findById(id);

        assertNotNull(clientRecupere);
        assertEquals(id, clientRecupere.getId());
        assertEquals("Jlassi", clientRecupere.getNom());
        assertEquals("PERMIS12345", clientRecupere.getNumPermis());

        // 4. UPDATE
        System.out.println("===== UPDATE =====");

        clientRecupere.setTelephone("55112233");

        ClientDTO clientModifie = clientService.update(id, clientRecupere);

        assertEquals("55112233", clientModifie.getTelephone());

        System.out.println("Client modifié : " + clientModifie);
    }
}
