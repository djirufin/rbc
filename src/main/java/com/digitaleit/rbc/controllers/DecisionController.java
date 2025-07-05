package com.digitaleit.rbc.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitaleit.rbc.model.DecisionNode;
import com.digitaleit.rbc.model.DecisionNodeDto;
import com.digitaleit.rbc.services.DecisionTreeBuilder;
import com.digitaleit.rbc.services.DecisionTreeService;

@RestController
@RequestMapping("/decision")
public class DecisionController {

    private final DecisionTreeBuilder treeBuilder;
    private final DecisionTreeService decisionTreeService;

    public DecisionController(DecisionTreeService decisionTreeService, DecisionTreeBuilder treeBuilder) {
        this.decisionTreeService = decisionTreeService;
        this.treeBuilder = treeBuilder;
    }

    @PostMapping("/evaluate")
    public String evaluate(@RequestBody Map<String, Object> input) {
        return decisionTreeService.evaluate(input);
    }

    @GetMapping("/tree")
    public DecisionNodeDto getTree() {
        DecisionNode<Map<String, Object>> tree = treeBuilder.buildTree();
        return convertToDto(tree);
    }

    @GetMapping("/tree/graphviz")
    public String getGraphvizTree() {
        DecisionNode<Map<String, Object>> root = treeBuilder.buildTree();
        StringBuilder sb = new StringBuilder("digraph DecisionTree {\n");
        generateDot(root, sb, "root");
        sb.append("}");
        return sb.toString();
    }

    // Méthodes convertToDto et generateDot à implémenter
    private DecisionNodeDto convertToDto(DecisionNode<Map<String, Object>> node) {
        if (node == null) return null;
        DecisionNodeDto dto = new DecisionNodeDto();
        dto.setCondition(node.getCondition() != null ? node.getCondition().toString() : null);
        dto.setAction(node.getAction() != null ? node.getAction().getClass().getSimpleName() : null);
        dto.setTrueBranch(convertToDto(node.getTrueBranch()));
        dto.setFalseBranch(convertToDto(node.getFalseBranch()));
        return dto;
    }

    private void generateDot(DecisionNode<Map<String, Object>> node, StringBuilder sb, String nodeName) {
        if (node == null) return;
        String trueName = nodeName + "_T";
        String falseName = nodeName + "_F";
        String label = node.getCondition() != null ? node.getCondition().toString().replaceAll("\"", "'") : node.getAction().getClass().getSimpleName();
        sb.append(String.format("%s [label=%s]\n", nodeName, label));
        if (node.getTrueBranch() != null) {
            sb.append(String.format("%s -> %s [label=true]\n", nodeName, trueName));
            generateDot(node.getTrueBranch(), sb, trueName);
        }
        if (node.getFalseBranch() != null) {
            sb.append(String.format("%s -> %s [label=false]\n", nodeName, falseName));
            generateDot(node.getFalseBranch(), sb, falseName);
        }
    }
}

