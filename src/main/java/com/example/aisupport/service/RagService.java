package com.example.aisupport.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import com.example.aisupport.repository.TicketRepository;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {
    private final EmbeddingModel embeddingModel;
    private EmbeddingStore<TextSegment> embeddingStore;
    private final TicketRepository ticketRepository;

    public RagService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();
        this.embeddingStore = PgVectorEmbeddingStore.builder()
                .host(getEnvOrDefault("PGVECTOR_HOST", "localhost"))
                .port(5432)
                .database(getEnvOrDefault("PGVECTOR_DB", "studentdb"))
                .user(getEnvOrDefault("PGVECTOR_USER", "priya-22652"))
                .password(getEnvOrDefault("PGVECTOR_PASSWORD", "postgres"))
                .table("ticket_embeddings")
                .dimension(384)
                .build();
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
    
    public void loadTicketsIntoVectorStore(){
        embeddingStore.removeAll();
        ticketRepository.findAll().forEach(ticket -> {
            String content = "Title : " + ticket.getTitle() + "\n Description : " + ticket.getDescription() + "\n Status : " + ticket.getStatus() + "\n Resolution : " + ticket.getResolution();
            TextSegment segment = TextSegment.from(content);
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        });
        System.out.println(">>> Ticket loaded into vector store");
    }

    public String findSimilarTickets(String question){
        Embedding questionEmbedding = embeddingModel.embed(TextSegment.from(question)).content();

        List<EmbeddingMatch<TextSegment>>  matches = embeddingStore.search(EmbeddingSearchRequest.builder().queryEmbedding(questionEmbedding).maxResults(5).build()).matches();
        if(matches.isEmpty()){
            return "No similar tickets found";
        }

        StringBuilder result = new StringBuilder();
        matches.forEach(match ->{
            result.append("---\n");
            result.append(match.embedded().text());
            result.append("\n");
        });
        return result.toString();
    }

    public String askAi(String question){
        String similarTickets = findSimilarTickets(question);
        String prompt = """
        You are a technical support assistant.
        
        Below are PAST RESOLVED tickets from our system:
        %s
        
        User's current question: %s
        
        IMPORTANT INSTRUCTIONS:
        - If any past ticket has a Resolution field, mention it EXACTLY.
        - Start your answer with "Based on past ticket: ..."
        - Be specific and technical, not generic.
        - If no resolution exists in past tickets, say "No past solution found."""
        .formatted(similarTickets,question);

        ChatLanguageModel qroq = OpenAiChatModel.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .apiKey(System.getenv("GROQ_API_KEY"))
                .modelName("openai/gpt-oss-20b")
                .build();
        return qroq.generate(prompt);
    }
}
