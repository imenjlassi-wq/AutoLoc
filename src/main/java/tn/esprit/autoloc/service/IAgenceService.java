
package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Agence;

public interface IAgenceService {

    Agence create(Agence agence);

    Agence findById(Long id);

    List<Agence> findAll();

    long count();

    Agence update(Long id, Agence agence);

    void deleteById(Long id);
}
