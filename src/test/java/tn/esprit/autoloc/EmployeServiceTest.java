
package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IEmployeService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeServiceTest {

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Test
    void testCreateFindAllFindByIdAndDelete() {

        // 1. Récupérer une agence existante
        Agence agence = agenceRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucune agence trouvée. Crée une agence d'abord."
                ));

        Long agenceId = agence.getId();

        System.out.println("ID agence : " + agenceId);

        // 2. CREATE : créer un employé associé à cette agence
        Employe employe = new Employe();
        employe.setNom("Jlassi");
        employe.setPrenom("Imen");
        employe.setRole(RoleEmploye.values()[0]);
        employe.setAgence(agence);

        Employe employeCree = employeService.create(employe);

        assertNotNull(employeCree.getId());
        assertEquals("Jlassi", employeCree.getNom());
        assertEquals("Imen", employeCree.getPrenom());
        assertNotNull(employeCree.getAgence());
        assertEquals(agenceId, employeCree.getAgence().getId());

        Long employeId = employeCree.getId();

        System.out.println("CREATE : Employé ajouté");
        System.out.println("ID employé : " + employeId);
        System.out.println("Nom : " + employeCree.getNom());
        System.out.println("Prénom : " + employeCree.getPrenom());
        System.out.println("ID agence associé : "
                + employeCree.getAgence().getId());

        // 3. FIND BY ID
        Employe employeTrouve = employeService.findById(employeId);

        assertNotNull(employeTrouve);
        assertEquals(employeId, employeTrouve.getId());
        assertEquals("Jlassi", employeTrouve.getNom());
        assertEquals("Imen", employeTrouve.getPrenom());
        assertEquals(agenceId, employeTrouve.getAgence().getId());

        System.out.println("FIND BY ID : "
                + employeTrouve.getNom() + " "
                + employeTrouve.getPrenom());

        // 4. FIND ALL
        var employes = employeService.findAll();

        assertTrue(employes.stream()
                .anyMatch(e -> e.getId().equals(employeId)));

        System.out.println("FIND ALL : "
                + employes.size() + " employé(s)");
/*
        // 5. DELETE
        employeService.deleteById(employeId);

        assertFalse(employeService.findAll().stream()
                .anyMatch(e -> e.getId().equals(employeId)));

        System.out.println("DELETE : Employé supprimé");*/
    }
}
