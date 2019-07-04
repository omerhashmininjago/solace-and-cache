package com.demonstrate.service;

import com.demonstrate.cache.TradeCacheUtil;
import com.demonstrate.domain.Trade;
import com.demonstrate.repo.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private final TradeCacheUtil tradeCacheUtil;

    @Transactional
    public void processTrade(Trade trade) {

        @Nullable Trade cachedTrade = tradeCacheUtil.getEntry(trade.getTradeId()).orElse(null);
        if (isDuplicate(trade, cachedTrade)) return;
        tradeCacheUtil.removeEntry(cachedTrade);
        tradeCacheUtil.addEntry(trade);
        tradeRepository.save(trade);
    }

    private boolean isDuplicate(Trade trade, Trade existingTrade) {
        if (!ObjectUtils.isEmpty(existingTrade)) {
            return existingTrade.getTradeState().value() >= trade.getTradeState().value() ? true : false;
        }
        return false;
    }
}