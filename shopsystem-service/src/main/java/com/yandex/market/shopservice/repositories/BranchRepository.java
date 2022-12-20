package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.model.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
