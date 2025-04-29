package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findByUserId(UUID user);
}
