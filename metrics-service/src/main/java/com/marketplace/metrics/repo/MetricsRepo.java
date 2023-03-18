package com.marketplace.metrics.repo;

import com.marketplace.metrics.model.ProductMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricsRepo extends JpaRepository<ProductMetric, Long> {
}
