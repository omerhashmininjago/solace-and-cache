package com.demonstrate.domain;

import com.demonstrate.error.InvalidTradeException;

public enum TradeStatusType {

    INITIAL(1),
    PROCESSING(2);

    private int lifeCycleStage;

    TradeStatusType(int lifeCycleStage) {
        this.lifeCycleStage = lifeCycleStage;
    }

    public static int getValue(TradeStatusType tradeStatusType) {
        if (null == tradeStatusType) throw new InvalidTradeException("Trade Status cannot be null or empty");
        for (TradeStatusType statusType : values()) {
            if (statusType.equals(tradeStatusType)) {
                return tradeStatusType.lifeCycleStage;
            }
        }
        throw new InvalidTradeException("Not a valid Trade Status");
    }
}
