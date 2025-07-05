package com.digitaleit.rbc.actions.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.digitaleit.rbc.actions.DecisionAction;

@Service("sendDiscountAction")
public class SendDiscountAction implements DecisionAction {
    public void execute(Map<String, Object> input) {
        // Logique pour envoyer une notification
    	System.out.println("Send discount action");
    }
}
