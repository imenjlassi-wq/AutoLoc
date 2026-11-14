
package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementDTO;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(target = "contratId", source = "contrat.id")
    PaiementDTO toDto(Paiement paiement);

    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);
}
