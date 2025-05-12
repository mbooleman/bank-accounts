package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaService kafkaService;

    public List<BankAccount> getBankAccountsByUserId(UUID userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    public Optional<BankAccount> findBankAccount(UUID bankAccountId) {
        return bankAccountRepository.findById(bankAccountId);
    }

    public Optional<BankAccount> findBankAccountByIBAN(IBAN iban) {
        return bankAccountRepository.findByIban(iban);
    }

    public void createBankAccount(BankAccount account) {
        bankAccountRepository.save(account);
    }

    @Transactional(rollbackOn = Exception.class)
    public void processTransaction(Transaction transaction) {
        bankAccountRepository.updateBalance(transaction.getFromAccount().getId(),
                transaction.getFromAccount().getBalance() - transaction.getAmount());

        kafkaService.sendMessage(String.format("account with id %s should receive %s", transaction.getToAccount().getId(),
                transaction.getAmount()));

        bankAccountRepository.updateBalance(transaction.getToAccount().getId(),
                transaction.getToAccount().getBalance() + transaction.getAmount());
    }


}
