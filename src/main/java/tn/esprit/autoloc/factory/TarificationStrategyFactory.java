package tn.esprit.autoloc.factory;

import tn.esprit.autoloc.domain.Reservation;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class TarificationStrategyFactory {

    private final List<TarificationStrategy> strategies;

    public TarificationStrategyFactory(List<TarificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public TarificationStrategy resoudre(Reservation reservation) {

        long jours = ChronoUnit.DAYS.between(
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (jours >= 7) {
            return trouverStrategy("LONGUE_DUREE");
        }

        if (jours <= 3 &&
                reservation.getDateDebut().getDayOfWeek().getValue() == 5) {
            return trouverStrategy("WEEKEND");
        }

        return trouverStrategy("STANDARD");
    }

    private TarificationStrategy trouverStrategy(String code) {
        return strategies.stream()
                .filter(strategy -> strategy.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Stratégie introuvable : " + code
                ));
    }
}