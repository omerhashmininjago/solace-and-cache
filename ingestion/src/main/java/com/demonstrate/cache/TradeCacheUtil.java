package com.demonstrate.cache;

import com.demonstrate.cache.factory.CacheFactory;
import com.demonstrate.domain.Trade;
import com.demonstrate.error.AppException;
import com.demonstrate.repo.TradeRepository;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class TradeCacheUtil {

    @Value("cache.maxsize")
    private int maxSize;

    @Value("cache.ttl")
    private long duration;

    @Value("cache.ttl.unit")
    private String timeUnit;

    @Autowired
    private Cache<Long, Optional<Trade>> tradeCache;

    @Autowired
    private TradeRepository tradeRepository;

    private Function<Long, Trade> LOAD_TRADE = (tradeId) -> tradeRepository.findByTradeId(tradeId);

    @PostConstruct
    public void initCache() {
        final Cache<Long, Optional<Trade>> loadingCache = CacheFactory.createCache(LOAD_TRADE, maxSize, duration, TimeUnit.valueOf(timeUnit));
        tradeCache = loadingCache;
    }

    public void addEntry(Trade trade) {
        tradeCache.put(trade.getTradeId(), Optional.of(trade));
    }

    public void removeEntry(Trade trade) {
        tradeCache.invalidate(trade);
    }

    public Optional<Trade> getEntry(Long tradeId) throws AppException {
        try {
            return tradeCache.get(tradeId, () -> Optional.ofNullable(tradeRepository.findByTradeId(tradeId)));
        } catch (ExecutionException e) {
            throw new AppException(e);
        }
    }

    @Scheduled(cron = "")
    public void refresh() {
        tradeCache.invalidateAll();
        initCache();
    }
}