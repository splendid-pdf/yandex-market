package com.marketplace.workflow.core.resilience;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor(staticName = "create")
public class Retry {
    private Backoff backoff;

    private final Integer maximumAttempts;
}