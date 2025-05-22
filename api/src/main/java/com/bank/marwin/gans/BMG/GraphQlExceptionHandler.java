package com.bank.marwin.gans.BMG;

import com.bank.marwin.gans.BMG.errors.NotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class GraphQlExceptionHandler implements DataFetcherExceptionResolver {

    private static final Map<Class<? extends Throwable>, ErrorType> EXCEPTION_TYPE_MAP = Map.of(
            NotFoundException.class, ErrorType.NOT_FOUND,
            IllegalArgumentException.class, ErrorType.BAD_REQUEST
    );

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
        ErrorType errorType = EXCEPTION_TYPE_MAP.getOrDefault(exception.getClass(), ErrorType.INTERNAL_ERROR);

        GraphQLError error = GraphqlErrorBuilder.newError(environment)
                .message(exception.getMessage())
                .errorType(errorType)
                .build();

        return Mono.just(List.of(error));
    }
}

