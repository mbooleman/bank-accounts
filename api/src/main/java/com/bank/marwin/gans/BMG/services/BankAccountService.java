package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BankAccount> getBankAccountsByUserId(UUID userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    public BankAccount createBankAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }
}
