package com.demonstrate.service;

import com.demonstrate.cache.TradeCacheUtil;
import com.demonstrate.domain.Trade;
import com.demonstrate.repo.TradeRepository;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeCacheUtil tradeCacheUtil;

    @Transactional
    public void processTrade(Trade trade) {

        @Nullable Trade cachedTrade = tradeCacheUtil.getEntry(trade.getTradeId());
        if (isDuplicate(trade, cachedTrade)) return;
        else {
            Trade persistedTrade = tradeRepository.findByTradeId(trade.getTradeId());
            if (isDuplicate(trade, persistedTrade)) return;
        }
        tradeCacheUtil.removeEntry(cachedTrade);
        tradeCacheUtil.addEntry(trade);
        tradeRepository.save(trade);
    }

    public boolean isDuplicate(Trade trade, Trade existingTrade) {
        if (ObjectUtils.isEmpty(existingTrade)) {
            return existingTrade.getTradeState().value() >= trade.getTradeState().value() ? true : false;
        }
        return false;
    }
}