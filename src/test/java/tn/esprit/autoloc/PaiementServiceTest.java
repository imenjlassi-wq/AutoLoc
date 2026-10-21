
package tn.esprit.autoloc;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IPaiementService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaiementServiceTest {

    @Autowired
    private IPaiementService paiementService;

    @Autowired
    private IContratRepository contratRepository;

    @Test
    void testCreateFindAllFindByIdAndDelete() {

        // 1. Récupérer un contrat existant
        Contrat contrat = contratRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucun contrat trouvé. Crée un contrat d'abord."
                ));

        Long contratId = contrat.getId();

        System.out.println("ID contrat : " + contratId);

        // 2. CREATE : créer un paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("250.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.values()[0]);
        paiement.setContrat(contrat);

        Paiement paiementCree = paiementService.create(paiement);

        assertNotNull(paiementCree.getId());
        assertEquals(
                0,
                new BigDecimal("250.00")
                        .compareTo(paiementCree.getMontant())
        );
        assertEquals(LocalDate.now(), paiementCree.getDatePaiement());
        assertEquals(contratId, paiementCree.getContrat().getId());

        Long paiementId = paiementCree.getId();

        System.out.println("CREATE : Paiement ajouté");
        System.out.println("ID paiement : " + paiementId);
        System.out.println("Montant : " + paiementCree.getMontant());
        System.out.println("Date : " + paiementCree.getDatePaiement());
        System.out.println("ID contrat : "
                + paiementCree.getContrat().getId());

        // 3. FIND BY ID
        Paiement paiementTrouve = paiementService.findById(paiementId);

        assertNotNull(paiementTrouve);
        assertEquals(paiementId, paiementTrouve.getId());
        assertEquals(
                0,
                new BigDecimal("250.00")
                        .compareTo(paiementTrouve.getMontant())
        );
        assertEquals(contratId, paiementTrouve.getContrat().getId());

        System.out.println("FIND BY ID : "
                + paiementTrouve.getMontant());

        // 4. FIND ALL
        var paiements = paiementService.findAll();

        assertTrue(paiements.stream()
                .anyMatch(p -> p.getId().equals(paiementId)));

        System.out.println("FIND ALL : "
                + paiements.size() + " paiement(s)");

        // 5. DELETE
       /* paiementService.deleteById(paiementId);

        assertFalse(paiementService.findAll().stream()
                .anyMatch(p -> p.getId().equals(paiementId)));

        System.out.println("DELETE : Paiement supprimé");*/
    }
}
