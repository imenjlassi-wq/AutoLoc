package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;

public interface IAgenceRepository extends JpaRepository<Agence,Long> {

}
