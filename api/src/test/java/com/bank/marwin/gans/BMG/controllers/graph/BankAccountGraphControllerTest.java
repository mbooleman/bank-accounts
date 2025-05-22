package com.bank.marwin.gans.BMG.controllers.graph;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@GraphQlTest(BankAccountGraphController.class)
@AutoConfigureGraphQlTester
public class BankAccountGraphControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private BankAccountRepository bankAccountRepository;

    @Test
    public void testBankDetails() {
        UUID userId = UUID.randomUUID();

        User user = new User(userId, "testing", "test@email.com", List.of("ADMIN", "USER"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT,
                "mijn account", 1234L, user, Currency.getInstance("EUR"));


        String query = String.format("""
                query {
                    bankDetails(id: "%s") {
                        id
                        iban
                        accountType
                        name
                        balance
                        user {
                            id
                            username
                            email
                            roles
                        }
                        currency
                    }
                }
                """, accountId);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        graphQlTester.document(query)
                .execute()
                .path("bankDetails.id").entity(String.class).isEqualTo(accountId.toString())
                .path("bankDetails.iban").entity(String.class).isEqualTo(account.getIban().getAccountNumber())
                .path("bankDetails.accountType").entity(String.class).isEqualTo(account.getAccountType().name())
                .path("bankDetails.name").entity(String.class).isEqualTo(account.getName())
                .path("bankDetails.balance").entity(Integer.class).isEqualTo(account.getBalance().intValue())
                .path("bankDetails.currency").entity(String.class).isEqualTo(account.getCurrency().getCurrencyCode())
                .path("bankDetails.user.id").entity(String.class).isEqualTo(user.getId().toString())
                .path("bankDetails.user.username").entity(String.class).isEqualTo(user.getUsername())
                .path("bankDetails.user.email").entity(String.class).isEqualTo(user.getEmail())
                .path("bankDetails.user.roles").entityList(String.class).isEqualTo(user.getRoles());
    }

    @Test
    void testBankNotFound() {
        UUID accountId = UUID.randomUUID();

        String query = String.format("""
                query {
                    bankDetails(id: "%s") {
                        id
                        iban
                        accountType
                        name
                        balance
                        user {
                            id
                            username
                            email
                            roles
                        }
                        currency
                    }
                }
                """, accountId);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        graphQlTester.document(query)
                .execute()
                .errors().satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage()).contains(String.format("Bank Account with id %s not found.",accountId));
                });
    }
}
