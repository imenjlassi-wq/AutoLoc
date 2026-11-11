package tn.esprit.autoloc.service.impl;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.observer.ReservationSubject;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IReservationService;

import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements IReservationService {


    private final IReservationRepository reservationRepository;



    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }


    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existante = findById(id);

        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        existante.setClient(reservation.getClient());
        existante.setVehicule(reservation.getVehicule());
        existante.setContrat(reservation.getContrat());

        return reservationRepository.save(existante);
    }


    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }


    private final ReservationSubject reservationSubject;



    // OBSERVER : Création d'une réservation
    public Reservation creerReservation(Client client) {

        Reservation reservation = new Reservation();

        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        // Sauvegarder la réservation
        reservation = reservationRepository.save(reservation);

        // Notifier les observateurs
        reservationSubject.notifierConfirmation(reservation);

        return reservation;
    }

    // OBSERVER : Annulation d'une réservation
    public Reservation annulerReservation(Reservation reservation) {

        reservation.setStatut(StatutReservation.ANNULEE);

        reservation = reservationRepository.save(reservation);

        log.info("Reservation #{} annulée pour le client {}",
                reservation.getId(),
                reservation.getClient().getEmail());

        reservationSubject.notifierAnnulation(reservation);

        return reservation;
    }


}
