package com.demonstrate.repo;

import com.demonstrate.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<Trade> findByTradeId(Long tradeId);
}

