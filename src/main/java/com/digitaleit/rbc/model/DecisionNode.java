package com.digitaleit.rbc.model;

import java.util.Map;
import java.util.function.Predicate;

import com.digitaleit.rbc.actions.DecisionAction;

import lombok.Data;

@Data
public class DecisionNode<T> {
    private Predicate<T> condition;
    private DecisionNode<T> trueBranch;
    private DecisionNode<T> falseBranch;
    private DecisionAction action;

    public DecisionNode(Predicate<T> condition, DecisionAction action) {
        this.condition = condition;
        this.action = action;
    }

    @SuppressWarnings("unchecked")
	public void evaluate(T input) {
        if (condition == null && action != null) {
        	System.out.println(action);
            action.execute((Map<String, Object>) input);
        } else if (condition != null && condition.test(input)) {
            if (trueBranch != null) trueBranch.evaluate(input); else System.out.println("ici 1");
        } else {
            if (falseBranch != null) falseBranch.evaluate(input); else System.out.println("ici 2");;
        }
    }

    // Getters et setters
}
