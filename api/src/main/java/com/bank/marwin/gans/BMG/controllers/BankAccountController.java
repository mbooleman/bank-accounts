package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.BankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.CreateBankAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.IBANDto;
import com.bank.marwin.gans.BMG.controllers.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.BankAccountService;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody CreateBankAccountDto accountDto) {
        User user = userService.findUserById(accountDto.userId());
        if (user == null) {
            throw new UserNotFoundException(accountDto.userId());
        }
        BankAccount account = accountDto.toDomain(user);
        bankAccountService.createBankAccount(account);
        return ResponseEntity.ok(new BankAccountDto(account));
    }

    @GetMapping("/{iban}")
    public ResponseEntity<IBANDto> findBankAccount(@PathVariable IBANDto ibanDto) {
        IBAN iban = ibanDto.toDomain();

        return ResponseEntity.ok(new IBANDto(iban.getAccountNumber()));
    }


}


