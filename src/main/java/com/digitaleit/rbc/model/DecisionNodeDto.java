package com.digitaleit.rbc.model;

import lombok.Data;

@Data
public class DecisionNodeDto {
    private String condition;
    private String action;
    private DecisionNodeDto trueBranch;
    private DecisionNodeDto falseBranch;
}