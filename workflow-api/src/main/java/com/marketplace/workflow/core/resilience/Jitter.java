package com.marketplace.workflow.core.resilience;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(staticName = "create")
public class Jitter {
    private double factor;
}
