package com.demonstrate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TRADE")
@Getter
@Setter
public class Trade {

    @Id
    @Column(name = "TRADE_ID")
    private Long tradeId;

    @Column(name = "INSTRUMENT_TYPE")
    private String instrumentType;

    @Column(name = "TRADE_STATUS")
    @Enumerated(EnumType.STRING)
    private String tradeStatus;

    @Column(name = "TRADE_VAL_DATE")
    private LocalDate tradeValueDate;

    @Column(name = "TRADE_UPDATED_DATE")
    private LocalDate tradeUpdatedDate;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trade{");
        sb.append("tradeId=").append(tradeId);
        sb.append(", instrumentType='").append(instrumentType).append('\'');
        sb.append(", tradeStatus='").append(tradeStatus).append('\'');
        sb.append(", tradeValueDate=").append(tradeValueDate);
        sb.append(", tradeUpdatedDate=").append(tradeUpdatedDate);
        sb.append('}');
        return sb.toString();
    }
}