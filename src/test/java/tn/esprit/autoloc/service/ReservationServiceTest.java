
package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.observer.ReservationSubject;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.impl.ReservationServiceImpl;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private ReservationSubject reservationSubject;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void testCreerReservation() {

        Client client = new Client();
        client.setEmail("test@gmail.com");
        client.setTelephone("12345678");

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation =
                reservationService.creerReservation(client);

        verify(reservationRepository, times(1))
                .save(any(Reservation.class));

        verify(reservationSubject, times(1))
                .notifierConfirmation(reservation);
    }

    @Test
    void testAnnulerReservation() {

        Client client = new Client();
        client.setEmail("test@gmail.com");
        client.setTelephone("12345678");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setClient(client);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.annulerReservation(reservation);

        verify(reservationRepository, times(1))
                .save(reservation);

        verify(reservationSubject, times(1))
                .notifierAnnulation(reservation);

        verify(reservationSubject, times(0))
                .notifierConfirmation(reservation);
    }
}
