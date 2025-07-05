package com.digitaleit.rbc.actions.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.digitaleit.rbc.actions.DecisionAction;

@Service("sendSurveyAction")
public class SendSurveyAction implements DecisionAction {
    public void execute(Map<String, Object> input) {
        // Logique pour envoyer une notification
    	System.out.println("Send survey action");
    }
}
