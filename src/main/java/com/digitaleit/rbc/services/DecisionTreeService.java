package com.digitaleit.rbc.services;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.digitaleit.rbc.actions.DecisionAction;
import com.digitaleit.rbc.model.DecisionNodeEntity;
import com.digitaleit.rbc.model.repository.DecisionNodeRepository;

@Service
public class DecisionTreeService {

    private final DecisionNodeRepository repository;
    private final ApplicationContext context;

    public DecisionTreeService(DecisionNodeRepository repository, ApplicationContext context) {
        this.repository = repository;
        this.context = context;
    }

    public String evaluate(Map<String, Object> input) {
        Map<Long, DecisionNodeEntity> nodeMap = repository.findAll().stream()
            .collect(Collectors.toMap(DecisionNodeEntity::getId, Function.identity()));

        DecisionNodeEntity current = nodeMap.get(1L); // assuming root ID is 1

        while (current != null) {
            if (current.getAction() != null) {
                /*Runnable action = actionMap.get(current.getAction());
                System.out.println(current.getAction());
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("❌ No bean found for action: " + current.getAction());
                }*/
            	DecisionAction action = context.getBean(current.getAction(), DecisionAction.class);
                System.out.println(current);
            	action.execute(input);
            	return current.getAction();
            }

            boolean result = current.evaluate(input);
            Long nextId = result ? current.getTrueBranchId() : current.getFalseBranchId();
            current = (nextId != null) ? nodeMap.get(nextId) : null;
        }

        return null;
    }
}