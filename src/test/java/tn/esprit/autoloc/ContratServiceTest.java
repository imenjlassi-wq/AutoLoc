
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContratServiceTest {

    @Autowired
    private IContratService contratService;

    @Test
    void testContratService() {

        // 1. CREATE
        ContratDTO contrat = new ContratDTO();

        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("150.00"));
        contrat.setValide(true);

        ContratDTO contratEnregistre = contratService.create(contrat);

        assertNotNull(contratEnregistre);
        assertNotNull(contratEnregistre.getId());

        Long id = contratEnregistre.getId();

        System.out.println("===== CREATE =====");
        System.out.println(contratEnregistre);

        // 2. FIND ALL
        System.out.println("===== FIND ALL =====");

        assertTrue(
                contratService.findAll().stream()
                        .anyMatch(c -> c.getId().equals(id))
        );

        // 3. FIND BY ID
        System.out.println("===== FIND BY ID =====");

        ContratDTO contratRecupere = contratService.findById(id);

        assertNotNull(contratRecupere);
        assertEquals(id, contratRecupere.getId());
        assertEquals(
                0,
                new BigDecimal("150.00")
                        .compareTo(contratRecupere.getMontantTotal())
        );
        assertEquals(LocalDate.now(), contratRecupere.getDateSignature());
        assertTrue(contratRecupere.getValide());

        // 4. UPDATE
        System.out.println("===== UPDATE =====");

        contratRecupere.setMontantTotal(new BigDecimal("200.00"));
        contratRecupere.setValide(false);

        ContratDTO contratModifie = contratService.update(id, contratRecupere);

        assertEquals(
                0,
                new BigDecimal("200.00")
                        .compareTo(contratModifie.getMontantTotal())
        );
        assertFalse(contratModifie.getValide());

        System.out.println("Contrat modifié : " + contratModifie);
    }
}
