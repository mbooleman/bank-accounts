package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.models.TransactionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t WHERE t.status = ?1 ORDER BY t.timestamp ASC")
    Page<Transaction> getTransactionsForStatus(TransactionStatus status, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Transaction t set t.status = 'COMPLETED' where t.id = ?1")
    @Transactional
    int completeTransaction(UUID transactionId);

    @Query("SELECT COUNT(t.id) FROM Transaction t WHERE t.status = ?1")
    int countTransactionsForStatus(TransactionStatus status);
    }
