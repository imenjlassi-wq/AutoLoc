
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquipementServiceTest {

    @Autowired
    private IEquipementService equipementService;

    @Test
    void testCreateFindAllFindByIdAndDelete() {

        // 1. CREATE : créer un équipement
        Equipement equipement = new Equipement();
        equipement.setLibelle("Climatisation");

        Equipement equipementCree = equipementService.create(equipement);

        assertNotNull(equipementCree.getId());
        assertEquals("Climatisation", equipementCree.getLibelle());

        Long id = equipementCree.getId();

        System.out.println("CREATE : Équipement ajouté");
        System.out.println("ID équipement : " + id);
        System.out.println("Libellé : " + equipementCree.getLibelle());

        // 2. FIND BY ID : rechercher l'équipement
        Equipement equipementTrouve = equipementService.findById(id);

        assertNotNull(equipementTrouve);
        assertEquals(id, equipementTrouve.getId());
        assertEquals("Climatisation", equipementTrouve.getLibelle());

        System.out.println("FIND BY ID : "
                + equipementTrouve.getLibelle());

        // 3. FIND ALL : afficher tous les équipements
        var equipements = equipementService.findAll();

        assertTrue(equipements.stream()
                .anyMatch(e -> e.getId().equals(id)));

        System.out.println("FIND ALL : "
                + equipements.size() + " équipement(s)");

        // 4. DELETE : supprimer l'équipement
        equipementService.deleteById(id);

        assertFalse(equipementService.findAll().stream()
                .anyMatch(e -> e.getId().equals(id)));

        System.out.println("DELETE : Équipement supprimé");
    }
}
