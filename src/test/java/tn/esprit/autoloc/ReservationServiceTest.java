package tn.esprit.autoloc;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Test
    void testCreateFindAllFindByIdAndDelete() {

        // 1. Récupérer un client existant
        Long clientId = clientRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucun client trouvé. Crée un client d'abord."
                ))
                .getId();

        // 2. Récupérer un véhicule existant
        Long vehiculeId = vehiculeRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucun véhicule trouvé. Crée un véhicule d'abord."
                ))
                .getIdVehicule();

        // 3. Préparer le DTO
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setClientId(clientId);
        dto.setVehiculeId(vehiculeId);
        dto.setDateDebut(LocalDate.now());
        dto.setDateFin(LocalDate.now().plusDays(3));

        // 4. CREATE
        ReservationResponseDTO reservationCreee =
                reservationService.create(dto);

        assertNotNull(reservationCreee);
        assertNotNull(reservationCreee.getId());
        assertEquals(clientId, reservationCreee.getClientId());
        assertEquals(vehiculeId, reservationCreee.getVehiculeId());
        assertNotNull(reservationCreee.getTarifEstime());

        Long reservationId = reservationCreee.getId();

        System.out.println("CREATE : Réservation ajoutée");
        System.out.println("ID réservation : " + reservationId);

        // 5. FIND BY ID
        ReservationResponseDTO reservationTrouvee =
                reservationService.findById(reservationId);

        assertNotNull(reservationTrouvee);
        assertEquals(reservationId, reservationTrouvee.getId());
        assertEquals(clientId, reservationTrouvee.getClientId());

        System.out.println("FIND BY ID : réservation "
                + reservationTrouvee.getId());

        // 6. FIND ALL
        var reservations = reservationService.findAll();

        assertTrue(reservations.stream()
                .anyMatch(r -> r.getId().equals(reservationId)));

        System.out.println("FIND ALL : "
                + reservations.size() + " réservation(s)");

        // 7. DELETE
        reservationService.deleteById(reservationId);

        assertFalse(reservationService.findAll().stream()
                .anyMatch(r -> r.getId().equals(reservationId)));

        System.out.println("DELETE : Réservation supprimée");
    }
}