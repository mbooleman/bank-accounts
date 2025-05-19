package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.*;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
public class AsyncTransactionServiceIntegrationTest {
    static Network network = Network.newNetwork();


//    @Container
//    static ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer(
//            "confluentinc/cp-kafka:7.6.0")
//            .withNetwork(network)
//            .withNetworkAliases("kafka")
//            .withEnv("CLUSTER_ID", "anHFXiFk4kKfQfdM1S28Xw")
//            .withEnv("KAFKA_CLUSTER_ID", "cluster-1")
//            .withEnv("KAFKA_PROCESS_ROLES", "broker,controller")
//            .withEnv("KAFKA_NODE_ID", "1")
//            .withEnv("KAFKA_CONTROLLER_QUORUM_VOTERS", "1@kafka:9094")
//            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
//            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
//            .withEnv("KAFKA_SCHEMA_REGISTRY_URL", "http://schema-registry:8081")
//            .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9094,BROKER://0.0.0.0:9093")
//            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "CONTROLLER:PLAINTEXT, PLAINTEXT:PLAINTEXT,BROKER:PLAINTEXT")
//            .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "BROKER")
//            .withEnv("KAFKA_CONTROLLER_LISTENER_NAMES", "CONTROLLER");
//
//
//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
//            "postgres:17.4-alpine").withDatabaseName("integration-tests-db").withUsername("sa").withPassword("sa");
//
//    @Container
//    static GenericContainer<?> schemaRegistryContainer = new GenericContainer<>(
//            DockerImageName.parse("confluentinc/cp-schema-registry:7.6.0"))
//            .withExposedPorts(8081)
//            .dependsOn(kafkaContainer)
//            .withNetwork(network)
//            .withNetworkAliases("schema-registry")
//            .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "PLAINTEXT://kafka:9092")
//            .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
//            .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.schema-registry-url",
                () -> "http://" + schemaRegistryContainer.getHost() + ":" + schemaRegistryContainer.getMappedPort(
                        8081));
    }

    @Autowired
    private AsyncTransactionService transactionService;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private record SetupData(User user, BankAccount fromAccount, BankAccount toAccount, Transaction transaction) {
    }

    private SetupData setupData() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));
        userRepository.save(user);
        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID toAccountOd = UUID.randomUUID();
        BankAccount toAccount = new BankAccount(toAccountOd, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L,
                user, Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID fromAccountId = UUID.randomUUID();
        BankAccount fromAccount = new BankAccount(fromAccountId, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2",
                200L, user, Currency.getInstance("EUR"));
        accountRepository.saveAll(List.of(toAccount, fromAccount));

        Transaction transaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "test description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        return new SetupData(user, fromAccount, toAccount, transaction);
    }

    @Test
    void runTransactionExecutorWorksForPendingTransaction() {
        setupData();
//        transactionService.runTransactionExecutor();

    }
}