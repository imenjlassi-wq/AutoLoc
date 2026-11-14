
package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(
        name = "Contrats",
        description = "Gestion des contrats de location"
)
public class ContratController {

    private final IContratService contratService;

    // 1. Lister tous les contrats
    @Operation(summary = "Lister tous les contrats")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des contrats récupérée avec succès"
    )
    @GetMapping
    public List<ContratDTO> findAll() {
        return contratService.findAll();
    }

    // 2. Consulter un contrat par son ID
    @Operation(summary = "Consulter un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ContratDTO findById(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id
    ) {
        return contratService.findById(id);
    }

    // 3. Créer un contrat
    @Operation(summary = "Créer un contrat")
    @ApiResponse(
            responseCode = "201",
            description = "Contrat créé avec succès"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO create(@RequestBody ContratDTO dto) {
        return contratService.create(dto);
    }

    // 4. Modifier un contrat
    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @PutMapping("/{id}")
    public ContratDTO update(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id,
            @RequestBody ContratDTO dto
    ) {
        return contratService.update(id, dto);
    }

    // 5. Supprimer un contrat
    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id
    ) {
        contratService.deleteById(id);
    }
}
