package tn.esprit.autoloc;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;

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
        Long contratId = contratRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucun contrat trouvé. Crée un contrat d'abord."
                ))
                .getId();

        System.out.println("ID contrat : " + contratId);

        // 2. CREATE : préparer le DTO
        PaiementDTO dto = new PaiementDTO();
        dto.setMontant(new BigDecimal("250.00"));
        dto.setDatePaiement(LocalDate.now());
        dto.setModePaiement("ESPECES");
        dto.setContratId(contratId);

        PaiementDTO paiementCree = paiementService.create(dto);

        assertNotNull(paiementCree.getId());
        assertEquals(0, new BigDecimal("250.00")
                .compareTo(paiementCree.getMontant()));
        assertEquals(LocalDate.now(), paiementCree.getDatePaiement());
        assertEquals(contratId, paiementCree.getContratId());

        Long paiementId = paiementCree.getId();

        System.out.println("CREATE : Paiement ajouté");
        System.out.println("ID paiement : " + paiementId);

        // 3. FIND BY ID
        PaiementDTO paiementTrouve =
                paiementService.findById(paiementId);

        assertNotNull(paiementTrouve);
        assertEquals(paiementId, paiementTrouve.getId());
        assertEquals(contratId, paiementTrouve.getContratId());

        System.out.println("FIND BY ID : " + paiementTrouve.getMontant());

        // 4. FIND ALL
        var paiements = paiementService.findAll();

        assertTrue(paiements.stream()
                .anyMatch(p -> p.getId().equals(paiementId)));

        System.out.println("FIND ALL : "
                + paiements.size() + " paiement(s)");

        // 5. DELETE
        paiementService.deleteById(paiementId);

        assertFalse(paiementService.findAll().stream()
                .anyMatch(p -> p.getId().equals(paiementId)));

        System.out.println("DELETE : Paiement supprimé");
    }
}