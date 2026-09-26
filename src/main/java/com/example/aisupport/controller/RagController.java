package com.example.aisupport.controller;

import com.example.aisupport.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {
    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/rag/load")
    public String loadTickets() {
        System.out.println(">>> RAG CONTROLLER HIT");
        ragService.loadTicketsIntoVectorStore();
        return "Ticket loaded into vector store";
    }

    @GetMapping("/rag/search")
    public String search(@RequestParam String question) {
        return ragService.findSimilarTickets(question);
    }

    @GetMapping("/rag/ask")
    public String ask(@RequestParam String question) {
        return ragService.askAi(question);
    }
}
