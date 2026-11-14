package tn.esprit.autoloc.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion des réservations")
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationRequestValidator reservationRequestValidator;

    @GetMapping
    public List<ReservationResponseDTO> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public ReservationResponseDTO findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {
            Map<String, String> erreurs = new HashMap<>();

            result.getFieldErrors().forEach(err ->
                    erreurs.put(err.getField(), err.getDefaultMessage()));

            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {
            Map<String, String> erreurs = new HashMap<>();

            result.getFieldErrors().forEach(err ->
                    erreurs.put(err.getField(), err.getDefaultMessage()));

            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}