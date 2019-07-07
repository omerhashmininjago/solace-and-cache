package com.demonstrate.domain;

public enum TradeStatusType {

    INITIAL(1),
    PROCESSING(2);

    private int lifeCycleStage;

    TradeStatusType(int lifeCycleStage) {
        this.lifeCycleStage = lifeCycleStage;
    }

}
