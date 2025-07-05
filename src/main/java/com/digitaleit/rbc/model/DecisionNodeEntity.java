package com.digitaleit.rbc.model;

import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DecisionNodeEntity {
    @Id
    private Long id;
    private String fieldName;
    private String operator;
    private String value;
    private Long trueBranchId;
    private Long falseBranchId;
    private String action;
    // Getters et setters
    
    public boolean evaluate(Map<String, Object> input) {
        Object inputValue = input.get(this.fieldName);
        if (inputValue == null || this.operator == null || this.value == null) {
            return false;
        }

        try {
            switch (this.operator) {
                case ">":
                    return Double.parseDouble(inputValue.toString()) > Double.parseDouble(this.value);
                case "<":
                    return Double.parseDouble(inputValue.toString()) < Double.parseDouble(this.value);
                case ">=":
                    return Double.parseDouble(inputValue.toString()) >= Double.parseDouble(this.value);
                case "<=":
                    return Double.parseDouble(inputValue.toString()) <= Double.parseDouble(this.value);
                case "==":
                    return inputValue.toString().equalsIgnoreCase(this.value);
                case "!=":
                    return !inputValue.toString().equalsIgnoreCase(this.value);
                default:
                    System.out.println("❌ Unsupported operator: " + this.operator);
                    return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Error parsing numbers: " + e.getMessage());
            return false;
        }
    }
}

