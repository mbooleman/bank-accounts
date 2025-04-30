package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.BankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.CreateBankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.IBANDto;
import com.bank.marwin.gans.BMG.controllers.errors.BankAccountNotFoundByIBANException;
import com.bank.marwin.gans.BMG.controllers.errors.BankAccountNotFoundException;
import com.bank.marwin.gans.BMG.controllers.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.BankAccountService;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody CreateBankAccountDto accountDto) {
        User user = userService.findUserById(accountDto.userId())
                .orElseThrow(() -> new UserNotFoundException(accountDto.userId()));

        BankAccount account = accountDto.toDomain(user);
        bankAccountService.createBankAccount(account);

        return ResponseEntity.ok(new BankAccountDto(account));
    }

    @GetMapping(value = "/{iban}")
    public ResponseEntity<BankAccountDto> findBankAccountByIBAN(@PathVariable String iban) {
        IBAN validIban = new IBAN(iban);
        BankAccount account = bankAccountService.findBankAccountByIBAN(validIban)
                .orElseThrow(() -> new BankAccountNotFoundByIBANException(validIban));

        return ResponseEntity.ok(new BankAccountDto(account));
    }

    @GetMapping(params = "bankAccountId")
    public ResponseEntity<BankAccountDto> findBankAccount(@RequestParam UUID bankAccountId) {
        BankAccount account = bankAccountService.findBankAccount(bankAccountId)
                .orElseThrow(() -> new BankAccountNotFoundException(bankAccountId));

        return ResponseEntity.ok(new BankAccountDto(account));
    }


}


