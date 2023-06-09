package com.marketplace.workflow.core.resilience;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor(staticName = "create")
public class Backoff {

    private Jitter jitter;
    private final Duration delay;

    public Backoff withJitter(double factor) {
        this.jitter = Jitter.create().factor(factor);
        return this;
    }

    public long getDelayInNanos() {
        return delay.toNanos();
    }
}
