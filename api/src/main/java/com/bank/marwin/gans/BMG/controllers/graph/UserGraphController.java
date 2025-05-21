package com.bank.marwin.gans.BMG.controllers.graph;

import com.bank.marwin.gans.BMG.controllers.graph.dto.BankAccountGraph;
import com.bank.marwin.gans.BMG.controllers.rest.dtos.UserGraph;
import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundException;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class UserGraphController {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountRepository accountRepo;

    @QueryMapping
    public UserGraph userDetails(@Argument String id) {
        UUID userId = UUID.fromString(id);
        User user = userService.findUserById(userId).get();
        return new UserGraph(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    @MutationMapping
    public UserGraph createUser(@Argument String username, @Argument String email, @Argument List<String> roles) {
        User user = new User(null, username, email, roles);
        userService.createUser(user);
        System.out.println("printing id " + user.getId());
        return new UserGraph(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles());
    }
}
