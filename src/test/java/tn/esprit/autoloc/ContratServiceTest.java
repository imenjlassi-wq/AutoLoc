
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContratServiceTest {

    @Autowired
    private IContratService contratService;

    @Test
    void testContratService() {

        // 1. CREATE : créer un contrat
        Contrat contrat = new Contrat();

        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("150.00"));
        contrat.setValide(true);

        Contrat contratEnregistre = contratService.create(contrat);

        assertNotNull(contratEnregistre);
        assertNotNull(contratEnregistre.getId());

        Long id = contratEnregistre.getId();

        System.out.println("===== CREATE =====");
        System.out.println(contratEnregistre);

        // 2. FIND ALL : afficher tous les contrats
        System.out.println("===== FIND ALL =====");

        contratService.findAll().forEach(System.out::println);

        assertTrue(
                contratService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        // 3. FIND BY ID : rechercher le contrat
        System.out.println("===== FIND BY ID =====");

        Contrat contratRecupere = contratService.findById(id);

        assertNotNull(contratRecupere);
        assertEquals(id, contratRecupere.getId());
        assertEquals(
                0,
                new BigDecimal("150.00")
                        .compareTo(contratRecupere.getMontantTotal())
        );
        assertEquals(LocalDate.now(), contratRecupere.getDateSignature());
        assertTrue(contratRecupere.isValide());

        System.out.println(contratRecupere);
/*
        // 4. DELETE : supprimer le contrat
        System.out.println("===== DELETE =====");

        contratService.deleteById(id);

        System.out.println("Contrat supprimé : " + id);

        // 5. Vérifier que le contrat n'existe plus
        assertFalse(
                contratService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        System.out.println(
                "Vérification : le contrat a été supprimé avec succès."
        );*/
    }
}
