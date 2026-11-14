
package tn.esprit.autoloc.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.factory.TarificationStrategyFactory;
import tn.esprit.autoloc.observer.ReservationSubject;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;
    private final TarificationStrategyFactory tarificationStrategyFactory;
    private final ReservationSubject reservationSubject;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", dto.getClientId()));

        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);

        // Calcul du tarif selon la stratégie sélectionnée
        double tarif = tarificationStrategyFactory
                .resoudre(saved)
                .calculerTarif(saved);

        ReservationResponseDTO response = reservationMapper.toDto(saved);
        response.setTarifEstime(BigDecimal.valueOf(tarif));

        // Notification Observer
        reservationSubject.notifierConfirmation(saved);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));

        ReservationResponseDTO response = reservationMapper.toDto(reservation);

        double tarif = tarificationStrategyFactory
                .resoudre(reservation)
                .calculerTarif(reservation);

        response.setTarifEstime(BigDecimal.valueOf(tarif));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {

        return reservationRepository.findAll()
                .stream()
                .map(reservation -> {
                    ReservationResponseDTO response =
                            reservationMapper.toDto(reservation);

                    double tarif = tarificationStrategyFactory
                            .resoudre(reservation)
                            .calculerTarif(reservation);

                    response.setTarifEstime(BigDecimal.valueOf(tarif));

                    return response;
                })
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", dto.getClientId()));

        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));

        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());

        Reservation updated = reservationRepository.save(reservation);

        ReservationResponseDTO response = reservationMapper.toDto(updated);

        double tarif = tarificationStrategyFactory
                .resoudre(updated)
                .calculerTarif(updated);

        response.setTarifEstime(BigDecimal.valueOf(tarif));

        return response;
    }

    @Override
    public void deleteById(Long id) {

        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }

        reservationRepository.deleteById(id);
    }

    // OBSERVER : Création d'une réservation
    public Reservation creerReservation(Client client) {

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        reservation = reservationRepository.save(reservation);

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
