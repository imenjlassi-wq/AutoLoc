
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgenceServiceTest {

    @Autowired
    private IAgenceService agenceService;

    @Test
    void testAgenceService() {

        // 1. CREATE
        AgenceDTO agence = new AgenceDTO();
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setNom("Agence Test");
        agence.setTelephone("22123456");
        agence.setVille("Tunis");

        AgenceDTO agenceEnregistree = agenceService.create(agence);

        assertNotNull(agenceEnregistree);
        assertNotNull(agenceEnregistree.getId());

        Long id = agenceEnregistree.getId();

        System.out.println("===== CREATE =====");
        System.out.println(agenceEnregistree);

        // 2. FIND ALL
        System.out.println("===== FIND ALL =====");

        agenceService.findAll().forEach(System.out::println);

        assertTrue(
                agenceService.findAll().stream()
                        .anyMatch(a -> a.getId().equals(id))
        );

        // 3. FIND BY ID
        System.out.println("===== FIND BY ID =====");

        AgenceDTO agenceRecuperee = agenceService.findById(id);

        assertNotNull(agenceRecuperee);
        assertEquals(id, agenceRecuperee.getId());

        System.out.println(agenceRecuperee);

        // 4. UPDATE
        agence.setNom("Agence Test Modifiée");
        agence.setVille("Sousse");

        AgenceDTO agenceModifiee = agenceService.update(id, agence);

        assertEquals("Agence Test Modifiée", agenceModifiee.getNom());
        assertEquals("Sousse", agenceModifiee.getVille());

        System.out.println("===== UPDATE =====");
        System.out.println(agenceModifiee);

        // 5. COUNT
        long nombreAgences = agenceService.count();

        System.out.println("===== COUNT =====");
        System.out.println("Nombre d'agences : " + nombreAgences);

        assertTrue(nombreAgences > 0);

        // 6. DELETE
        agenceService.deleteById(id);

        System.out.println("===== DELETE =====");
        System.out.println("Agence supprimée : " + id);

        // 7. Vérifier que l'agence n'existe plus
        assertFalse(
                agenceService.findAll().stream()
                        .anyMatch(a -> a.getId().equals(id))
        );

        System.out.println("Vérification : l'agence n'existe plus.");
    }
}
