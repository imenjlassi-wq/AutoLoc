package tn.esprit.autoloc;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehiculeServiceTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Test
    void testCreateFindAllFindByIdUpdateAndDelete() {

        // 1. Récupérer une agence existante
        Agence agence = agenceRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucune agence trouvée dans la base de données"));

        // 2. Créer un véhicule avec VehiculeDTO
        VehiculeDTO vehicule = new VehiculeDTO();

        vehicule.setImmatriculation("TEST-123");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setTarifJournalier(new BigDecimal("120.00"));
        vehicule.setCategorie(CategorieVehicule.values()[0].name());
        vehicule.setStatut(StatutVehicule.values()[0].name());
        vehicule.setAgenceId(agence.getId());

        VehiculeDTO vehiculeCree = vehiculeService.create(vehicule);

        assertNotNull(vehiculeCree.getId());
        assertEquals("TEST-123", vehiculeCree.getImmatriculation());
        assertEquals("Toyota", vehiculeCree.getMarque());
        assertEquals(agence.getId(), vehiculeCree.getAgenceId());

        Long id = vehiculeCree.getId();

        System.out.println("CREATE - Véhicule créé, ID : " + id);

        // 3. Rechercher par ID
        VehiculeDTO vehiculeTrouve = vehiculeService.findById(id);

        assertNotNull(vehiculeTrouve);
        assertEquals(id, vehiculeTrouve.getId());
        assertEquals("Yaris", vehiculeTrouve.getModele());

        System.out.println("FIND BY ID - Véhicule trouvé : "
                + vehiculeTrouve.getImmatriculation());

        // 4. Récupérer tous les véhicules
        assertTrue(vehiculeService.findAll()
                .stream()
                .anyMatch(v -> v.getId().equals(id)));

        System.out.println("FIND ALL - Véhicule présent dans la liste");

        // 5. Modifier le véhicule
        VehiculeDTO vehiculeModifie = new VehiculeDTO();

        vehiculeModifie.setImmatriculation("TEST-456");
        vehiculeModifie.setMarque("Toyota");
        vehiculeModifie.setModele("Yaris");
        vehiculeModifie.setTarifJournalier(new BigDecimal("150.00"));
        vehiculeModifie.setCategorie(CategorieVehicule.values()[0].name());
        vehiculeModifie.setStatut(StatutVehicule.values()[0].name());
        vehiculeModifie.setAgenceId(agence.getId());

        VehiculeDTO resultatUpdate =
                vehiculeService.update(id, vehiculeModifie);

        assertEquals(id, resultatUpdate.getId());
        assertEquals("TEST-456", resultatUpdate.getImmatriculation());
        assertEquals(new BigDecimal("150.00"),
                resultatUpdate.getTarifJournalier());

        System.out.println("UPDATE - Véhicule modifié : "
                + resultatUpdate.getImmatriculation());

        // 6. Supprimer le véhicule
        vehiculeService.deleteById(id);

        assertFalse(vehiculeService.findAll()
                .stream()
                .anyMatch(v -> v.getId().equals(id)));

        System.out.println("DELETE - Véhicule supprimé, ID : " + id);
    }
}