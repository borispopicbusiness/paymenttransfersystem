package org.borispopic.paymenttransfersystem.repository;

import org.borispopic.paymenttransfersystem.entity.LedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerRepository extends JpaRepository<LedgerEntity, UUID> {
}
