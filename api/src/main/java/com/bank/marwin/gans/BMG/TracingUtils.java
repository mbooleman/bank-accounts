package com.bank.marwin.gans.BMG;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TracingUtils {

    @Autowired
    private Tracer tracer;

    public <T> T withSpan(String spanName, Supplier<T> supplier) {
        Span newSpan = tracer.nextSpan().name(spanName).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {
            return supplier.get();
        } finally {
            newSpan.end();
        }
    }

    public void withSpan(String spanName, Runnable runnable) {
        Span newSpan = tracer.nextSpan().name(spanName).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {
            runnable.run();
        } finally {
            newSpan.end();
        }
    }
}
