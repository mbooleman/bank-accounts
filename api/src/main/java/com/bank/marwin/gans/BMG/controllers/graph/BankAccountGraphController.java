package com.bank.marwin.gans.BMG.controllers.graph;

import com.bank.marwin.gans.BMG.controllers.graph.dto.BankAccountGraph;
import com.bank.marwin.gans.BMG.controllers.rest.dtos.UserGraph;
import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundException;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class BankAccountGraphController {
    @Autowired
    private BankAccountRepository accountRepo;

    @QueryMapping
    public BankAccountGraph bankDetails(@Argument String id) {
        UUID accountId = UUID.fromString(id);
        BankAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(accountId));

        User user = account.getUser();
        UserGraph userGraph = new UserGraph(user.getId().toString(), user.getUsername(), user.getEmail(),
                user.getRoles());
        return new BankAccountGraph(account.getId().toString(), account.getIban().getAccountNumber(),
                account.getAccountType().name(), account.getName(), account.getBalance(), userGraph,
                account.getCurrency().getCurrencyCode());
    }
}
