package com.amazon.atlas.health;

import com.codahale.metrics.health.HealthCheck;

public class AtlasHealthChecker extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
