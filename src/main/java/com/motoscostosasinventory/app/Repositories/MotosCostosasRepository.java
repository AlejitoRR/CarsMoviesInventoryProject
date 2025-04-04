package com.motoscostosasinventory.app.Repositories;


import com.motoscostosasinventory.app.Entities.MotosCostosasEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MotosCostosasRepository extends JpaRepository<MotosCostosasEntity, UUID>{

    Page<MotosCostosasEntity> findAllByMarcaContaining(String marca, Pageable pageable);

    @Override
    Page<MotosCostosasEntity> findAll(Pageable pageable);
}