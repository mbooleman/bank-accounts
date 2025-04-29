package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.commands.CreateBankAccountCommand;
import com.bank.marwin.gans.domain.BankAccount;
import com.bank.marwin.gans.domain.IBAN;
import com.bank.marwin.gans.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private CreateBankAccountCommand createBankAccountCommand;

    @PostMapping("")
    public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody BankAccountDto accountDto) {
        BankAccount account = accountDto.toDomain();
        System.out.println(account);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/{iban}")
    public ResponseEntity<IBANDto> findBankAccount(@PathVariable IBANDto ibanDto) {
        IBAN iban = ibanDto.toDomain();

        return ResponseEntity.ok(new IBANDto(iban.getAccountNumber()));
    }


}


