
package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.PaiementDTO;
import java.util.List;

public interface IPaiementService {

    PaiementDTO create(PaiementDTO dto);

    PaiementDTO findById(Long id);

    List<PaiementDTO> findAll();

    PaiementDTO update(Long id, PaiementDTO dto);

    void deleteById(Long id);
}
