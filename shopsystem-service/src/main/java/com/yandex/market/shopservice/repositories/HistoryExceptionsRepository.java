package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.model.HistoryExceptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryExceptionsRepository extends JpaRepository<HistoryExceptions, Long> {
}
