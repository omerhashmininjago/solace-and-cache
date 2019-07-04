package com.demonstrate.cache;

import com.demonstrate.cache.factory.CacheFactory;
import com.demonstrate.domain.Trade;
import com.demonstrate.repo.TradeRepository;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class TradeCacheUtil {

    @Value("")
    private int maxSize;

    @Value("")
    private long duration;

    @Value("")
    private String timeUnit;

    @Autowired
    private Cache<Long, Trade> tradeCache;

    @Autowired
    private TradeRepository tradeRepository;

    @PostConstruct
    public void initCache() {
        final Cache<Long, Trade> loadingCache = CacheFactory.createCache((tradeId) -> tradeRepository.findByTradeId(tradeId), maxSize, duration, TimeUnit.valueOf(timeUnit));
        tradeCache = loadingCache;
    }

    public void addEntry(Trade trade) {
        tradeCache.put(trade.getTradeId(), trade);
    }

    public void removeEntry(Trade trade) {
        tradeCache.invalidate(trade);
    }

    public Trade getEntry(Long tradeId) {
        return tradeCache.getIfPresent(tradeId);
    }

    @Scheduled(cron = "")
    public void refresh() {
        tradeCache.invalidateAll();
        initCache();
    }
}
