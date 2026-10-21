
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgenceServiceTest {

    @Autowired
    private IAgenceService agenceService;

    @Test
    void testAgenceService() {

        // 1. CREATE
        Agence agence = new Agence();
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setNom("Agence Test");
        agence.setTelephone("22123456");
        agence.setVille("Tunis");

        Agence agenceEnregistree = agenceService.create(agence);

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
        Agence agenceRecuperee = agenceService.findById(id);

        assertNotNull(agenceRecuperee);
        assertEquals(id, agenceRecuperee.getId());

        System.out.println(agenceRecuperee);

        // 4. COUNT
        long nombreAgences = agenceService.count();

        System.out.println("===== COUNT =====");
        System.out.println("Nombre d'agences : " + nombreAgences);

        assertTrue(nombreAgences > 0);

        // 5. DELETE
        agenceService.deleteById(id);

        System.out.println("===== DELETE =====");
        System.out.println("Agence supprimée : " + id);

        // 6. Vérifier que l'agence n'existe plus
        assertFalse(
                agenceService.findAll().stream()
                        .anyMatch(a -> a.getId().equals(id))
        );

        System.out.println("Vérification : l'agence n'existe plus.");
    }
}
