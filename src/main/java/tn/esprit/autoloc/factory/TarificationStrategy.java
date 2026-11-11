package tn.esprit.autoloc.factory;

import tn.esprit.autoloc.domain.Reservation;

public interface TarificationStrategy {

    String getCode();

    double calculerTarif(Reservation reservation);
}