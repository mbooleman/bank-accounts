package com.bank.marwin.gans.BMG.controllers.graph.dto;

import com.bank.marwin.gans.BMG.controllers.rest.dtos.UserGraph;

public record BankAccountGraph(String id, String iban, String accountType, String name, Long balance, UserGraph user,
                               String currency) {

}
