
package tn.esprit.autoloc.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;

@Slf4j
@Component
public class EmailNotificationObserver implements IReservationObserver {

    @Override
    public void onReservationConfirmee(Reservation reservation) {
        log.info("[EMAIL] Confirmation envoyée au client {} pour la réservation #{}",
                reservation.getClient().getEmail(),
                reservation.getId());
    }

    @Override
    public void onReservationAnnulee(Reservation reservation) {
        log.info("[EMAIL] Annulation notifiée au client {} pour la réservation #{}",
                reservation.getClient().getEmail(),
                reservation.getId());
    }
}
