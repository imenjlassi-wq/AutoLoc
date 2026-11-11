package tn.esprit.autoloc.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;

    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {

        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();

        factory = new TarificationStrategyFactory(
                List.of(standard, weekend, longueDuree)
        );
    }

    @Test
    void doitRetournerLongueDureePour7JoursOuPlus() {

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 1, 1));
        reservation.setDateFin(LocalDate.of(2024, 1, 8));

        TarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat)
                .isInstanceOf(TarifLongueDureeStrategy.class);

        assertThat(resultat.getCode())
                .isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerLongueDureePourExactement7Jours() {

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 5, 1));
        reservation.setDateFin(LocalDate.of(2024, 5, 8));

        TarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode())
                .isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerWeekendPourCourtSejourVendredi() {

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 8));
        reservation.setDateFin(LocalDate.of(2024, 3, 10));

        TarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat)
                .isInstanceOf(TarifWeekendStrategy.class);

        assertThat(resultat.getCode())
                .isEqualTo("WEEKEND");
    }

    @Test
    void doitRetournerStandardPourMoyenneDuree() {

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 4));
        reservation.setDateFin(LocalDate.of(2024, 3, 8));

        TarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode())
                .isEqualTo("STANDARD");
    }
}
