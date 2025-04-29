package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.BankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.IBANDto;
import com.bank.marwin.gans.BMG.services.CreateBankAccountCommand;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
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


