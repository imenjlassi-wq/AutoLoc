package tn.esprit.autoloc.factory;

import tn.esprit.autoloc.domain.Reservation;

public class TarifWeekendStrategy implements TarificationStrategy {

    @Override
    public String getCode() {
        return "WEEKEND";
    }

    @Override
    public double calculerTarif(Reservation reservation) {
        return 120.0;
    }
}