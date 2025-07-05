package com.digitaleit.rbc.services;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.digitaleit.rbc.actions.DecisionAction;
import com.digitaleit.rbc.model.DecisionNode;
import com.digitaleit.rbc.model.DecisionNodeEntity;
import com.digitaleit.rbc.model.repository.DecisionNodeRepository;

@Service
public class DecisionTreeBuilder {

    private final DecisionNodeRepository repository;
    private final ApplicationContext context;
    
    public DecisionTreeBuilder(DecisionNodeRepository repository, ApplicationContext context) {
    	this.repository = repository;
    	this.context = context;
    }

    public DecisionNode<Map<String, Object>> buildTree() {
        Map<Long, DecisionNodeEntity> entities = repository.findAll().stream()
            .collect(Collectors.toMap(DecisionNodeEntity::getId, Function.identity()));
        
        return buildNodeRecursive(entities.get(1L), entities);
    }

    private DecisionNode<Map<String, Object>> buildNodeRecursive(DecisionNodeEntity entity, Map<Long, DecisionNodeEntity> allEntities) {
        Predicate<Map<String, Object>> condition = createPredicate(entity.getFieldName(), entity.getOperator(), entity.getValue());
        DecisionAction action = (entity.getAction() != null) ? context.getBean(entity.getAction(), DecisionAction.class) : null;
        DecisionNode<Map<String, Object>> node = new DecisionNode<>(condition, action);
        if (entity.getTrueBranchId() != null) {
            node.setTrueBranch(buildNodeRecursive(allEntities.get(entity.getTrueBranchId()), allEntities));
        }
        if (entity.getFalseBranchId() != null) {
            node.setFalseBranch(buildNodeRecursive(allEntities.get(entity.getFalseBranchId()), allEntities));
        }
        System.out.println(node);
        return node;
    }

    private Predicate<Map<String, Object>> createPredicate(String field, String operator, String valueStr) {
        return input -> {
            Object fieldValue = input.get(field);
            if (fieldValue == null) return false;
            switch (operator.toLowerCase()) {
                case "in":
                    return Arrays.asList(valueStr.split(",")).contains(fieldValue.toString());
                case "regex":
                    return Pattern.matches(valueStr, fieldValue.toString());
                case "contains":
                    return fieldValue.toString().contains(valueStr);
                case ">":
                    return Double.parseDouble(fieldValue.toString()) > Double.parseDouble(valueStr);
                case ">=":
                    return Double.parseDouble(fieldValue.toString()) >= Double.parseDouble(valueStr);
                case "<":
                    return Double.parseDouble(fieldValue.toString()) < Double.parseDouble(valueStr);
                case "<=":
                    return Double.parseDouble(fieldValue.toString()) <= Double.parseDouble(valueStr);
                case "==":
                    return fieldValue.toString().equals(valueStr);
                case "!=":
                    return !fieldValue.toString().equals(valueStr);
                default:
                    return false;
            }
        };
    }
}
