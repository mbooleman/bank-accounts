package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.BankAccountResponseAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.CombinedBankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.CreateBankAccountDto;
import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundByIBANException;
import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundException;
import com.bank.marwin.gans.BMG.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.CombinedBankAccount;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.CombineAccountService;
import com.bank.marwin.gans.BMG.services.BankAccountService;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private CombineAccountService combineAccountService;

    @PostMapping("")
    public ResponseEntity<BankAccountResponseAccountDto> createBankAccount(
            @RequestBody CreateBankAccountDto accountDto) {
        User user = userService.findUserById(accountDto.userId())
                .orElseThrow(() -> new UserNotFoundException(accountDto.userId()));

        BankAccount account = accountDto.toDomain(user);
        bankAccountService.createBankAccount(account);

        return ResponseEntity.ok(new BankAccountResponseAccountDto(account));
    }

    @GetMapping(value = "/{iban}")
    public ResponseEntity<BankAccountResponseAccountDto> findBankAccountByIBAN(@PathVariable String iban) {
        IBAN validIban = new IBAN(iban);
        BankAccount account = bankAccountService.findBankAccountByIBAN(validIban)
                .orElseThrow(() -> new BankAccountNotFoundByIBANException(validIban));

        return ResponseEntity.ok(new BankAccountResponseAccountDto(account));
    }

    @GetMapping(params = "bankAccountId")
    public ResponseEntity<BankAccountResponseAccountDto> findBankAccount(@RequestParam UUID bankAccountId) {
        BankAccount account = bankAccountService.findBankAccount(bankAccountId)
                .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));

        return ResponseEntity.ok(new BankAccountResponseAccountDto(account));
    }

    @PostMapping("/combine/{userId}")
    public ResponseEntity<CombinedBankAccountDto> combineAccounts(@PathVariable UUID userId,
                                                                  @RequestParam String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        CombinedBankAccount combinedBankAccount = combineAccountService.combineAccounts(userId, currency);
        return ResponseEntity.ok(new CombinedBankAccountDto(combinedBankAccount));
    }
}


