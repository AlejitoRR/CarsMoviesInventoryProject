package com.motoscostosasinventory.app.Controllers;

import com.motoscostosasinventory.app.Entities.MotosCostosasEntity;
import com.motoscostosasinventory.app.Services.MotosCostosasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/motoscostosas")
@Validated
public class MotosCostosasController {

    private final MotosCostosasService motosCostosasService;

    public MotosCostosasController(MotosCostosasService motosCostosasService) {
        this.motosCostosasService = motosCostosasService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMotosCostosas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "marca,asc") String[] sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            return motosCostosasService.getAllMotos(pageable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dirección de ordenamiento inválida. Usa 'asc' o 'desc'.");
        }
    }

    private Sort.Order parseSort(String[] sort) {
        if (sort.length < 2) {
            throw new IllegalArgumentException("El parámetro sort debe incluir campo y dirección (e.g., 'marca,desc').");
        }

        String property = sort[0];
        String direction = sort[1].toLowerCase();

        List<String> validDirections = Arrays.asList("asc", "desc");
        if (!validDirections.contains(direction)) {
            throw new IllegalArgumentException("Dirección de ordenamiento inválida. Usa 'asc' o 'desc'.");
        }

        return new Sort.Order(Sort.Direction.fromString(direction), property);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMotoById(@PathVariable UUID id) {
        return motosCostosasService.getMotoById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getMotosByMarca(
            @RequestParam String marca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "marca,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return motosCostosasService.getMotosByMarca(marca, pageable);
    }

    @PostMapping
    public ResponseEntity<?> insertMoto(@Valid @RequestBody MotosCostosasEntity motosCostosasEntity) {
        return motosCostosasService.addMoto(motosCostosasEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMoto(@PathVariable UUID id, @Valid @RequestBody MotosCostosasEntity motosCostosasEntity) {
        return motosCostosasService.updateMoto(id, motosCostosasEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMoto(@PathVariable UUID id) {
        return motosCostosasService.deleteMoto(id);
    }
}
