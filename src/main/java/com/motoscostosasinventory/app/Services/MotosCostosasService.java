package com.motoscostosasinventory.app.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.motoscostosasinventory.app.Repositories.MotosCostosasRepository;
import com.motoscostosasinventory.app.Entities.MotosCostosasEntity;

import java.util.*;

@Service
public class MotosCostosasService {

    private final MotosCostosasRepository motosCostosasRepository;

    public MotosCostosasService(MotosCostosasRepository motosCostosasRepository) {
        this.motosCostosasRepository = motosCostosasRepository;
    }

    public ResponseEntity<?> getAllMotos(Pageable pageable) {
        Page<MotosCostosasEntity> motos = motosCostosasRepository.findAll(pageable);
        return getResponseEntity(motos);
    }

    public ResponseEntity<?> getMotoById(UUID id) {
        Optional<MotosCostosasEntity> moto = motosCostosasRepository.findById(id);
        if (moto.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("Status", String.format("Moto with ID %s not found.", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(Collections.singletonMap("Moto", moto.get()));
    }

    public ResponseEntity<?> getMotosByMarca(String marca, Pageable pageable) {
        Page<MotosCostosasEntity> motos = motosCostosasRepository.findAllByMarcaContaining(marca, pageable);
        return getResponseEntity(motos);
    }

    private ResponseEntity<?> getResponseEntity(Page<MotosCostosasEntity> motos) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElements", motos.getTotalElements());
        response.put("TotalPages", motos.getTotalPages());
        response.put("CurrentPage", motos.getNumber());
        response.put("NumberOfElements", motos.getNumberOfElements());
        response.put("Motos", motos.getContent());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> addMoto(MotosCostosasEntity motoToAdd) {
        Page<MotosCostosasEntity> existingMotos = motosCostosasRepository.findAllByMarcaContaining(
                motoToAdd.getMarca(),
                Pageable.unpaged());
        if (existingMotos.getTotalElements() > 0) {
            return new ResponseEntity<>(Collections.singletonMap("Status", String.format("Moto already exists with %d coincidences.", existingMotos.getTotalElements())), HttpStatus.CONFLICT);
        } else {
            MotosCostosasEntity savedMoto = motosCostosasRepository.save(motoToAdd);
            return new ResponseEntity<>(Collections.singletonMap("Status", String.format("Added Moto with ID %s", savedMoto.getId())), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<?> updateMoto(UUID id, MotosCostosasEntity motoToUpdate) {
        Optional<MotosCostosasEntity> moto = motosCostosasRepository.findById(id);
        if (moto.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("Status", String.format("Moto with ID %s not found.", id)), HttpStatus.NOT_FOUND);
        }
        MotosCostosasEntity existingMoto = moto.get();

        existingMoto.setMarca(motoToUpdate.getMarca());
        existingMoto.setModelo(motoToUpdate.getModelo());
        existingMoto.setTopSpeed(motoToUpdate.getTopSpeed());

        motosCostosasRepository.save(existingMoto);

        return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Updated Moto with ID %s", existingMoto.getId())));
    }

    public ResponseEntity<?> deleteMoto(UUID id) {
        Optional<MotosCostosasEntity> moto = motosCostosasRepository.findById(id);
        if (moto.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("Status", String.format("Moto with ID %s doesn't exist.", id)), HttpStatus.NOT_FOUND);
        }
        motosCostosasRepository.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Deleted Moto with ID %s", id)));
    }

}
