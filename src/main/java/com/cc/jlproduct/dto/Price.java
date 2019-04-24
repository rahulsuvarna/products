package com.cc.jlproduct.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class Price {
    private BigDecimal was;
    private BigDecimal then1;
    private BigDecimal then2;
    private BigDecimal now;
    private Currency currency;

}
