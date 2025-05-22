package com.bank.marwin.gans.BMG.controllers.graph;

import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@GraphQlTest(UserGraphController.class)
@AutoConfigureGraphQlTester
public class UserGraphControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private UserService userService;

    @Test
    public void testUserDetails() {
        UUID userId = UUID.randomUUID();

        User user = new User(userId, "testing", "test@email.com", List.of("ADMIN", "USER"));

        String query = String.format("""
                query {
                    userDetails(id: "%s") {
                        id
                        username
                        email
                        roles
                    }
                }
                """, userId);

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        graphQlTester.document(query)
                .execute()
                .path("userDetails.id").entity(String.class).isEqualTo(userId.toString())
                .path("userDetails.username").entity(String.class).isEqualTo(user.getUsername())
                .path("userDetails.email").entity(String.class).isEqualTo(user.getEmail())
                .path("userDetails.roles").entityList(String.class).isEqualTo(user.getRoles());
    }

    @Test
    void testUserDetailsNotFound() {
        UUID userId = UUID.randomUUID();
        String query = String.format("""
                query {
                    userDetails(id: "%s") {
                        id
                        username
                        email
                        roles
                    }
                }
                """, userId);

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        graphQlTester.document(query)
                .execute()
                .errors().satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage()).contains(String.format("User with id %s not found.",userId));
                });
    }

    @Test
    void testCreateUser() {
        String username = "testuser";
        String email = "test@email.com";
        List<String> roles = List.of("ADMIN", "USER");
        String mutation = String.format("""
        mutation {
            createUser(username: "%s", email: "%s", roles: ["ADMIN","USER"]) {
                id
                username
                email
                roles
            }
        }
        """,username,email,roles);

        graphQlTester.document(mutation)
                .execute()
                .path("createUser.username").entity(String.class).isEqualTo(username)
                .path("createUser.email").entity(String.class).isEqualTo(email)
                .path("createUser.roles").entityList(String.class).isEqualTo(roles);
    }

}
