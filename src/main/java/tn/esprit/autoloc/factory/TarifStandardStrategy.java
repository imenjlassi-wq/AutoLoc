package tn.esprit.autoloc.factory;

import tn.esprit.autoloc.domain.Reservation;

public class TarifStandardStrategy implements TarificationStrategy {

    @Override
    public String getCode() {
        return "STANDARD";
    }

    @Override
    public double calculerTarif(Reservation reservation) {
        return 100.0;
    }
}