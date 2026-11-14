package tn.esprit.autoloc.web.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {

    private Long id;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être strictement positif")
    private BigDecimal montant;

    private LocalDate datePaiement;

    @NotBlank(message = "Le mode de paiement est obligatoire")
    private String modePaiement;

    @NotNull(message = "L'identifiant du contrat est obligatoire")
    private Long contratId;
}