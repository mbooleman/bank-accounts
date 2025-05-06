package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findByUserId(UUID userId);

    Optional<BankAccount> findByIban(IBAN iban);

    @Modifying(clearAutomatically = true)
    @Query("update BankAccount account set account.balance = ?2 where id = ?1")
    void updateBalance(UUID bankAccountId, Long newBalance);
}
