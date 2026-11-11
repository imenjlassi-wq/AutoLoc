package tn.esprit.autoloc.factory;

import tn.esprit.autoloc.domain.Reservation;

public class TarifLongueDureeStrategy implements TarificationStrategy {

    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }

    @Override
    public double calculerTarif(Reservation reservation) {
        return 80.0;
    }
}