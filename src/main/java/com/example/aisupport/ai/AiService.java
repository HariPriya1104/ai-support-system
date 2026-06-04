package com.example.aisupport.ai;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {
    private final ChatLanguageModel model;

    public AiService() {
        model = OpenAiChatModel.builder()
                .apiKey(System.getenv("GROQ_API_KEY"))
                .modelName("llama-3.1-8b-instant")
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    public String generateReply(String Title, String description)
    {
        String prompt = "A customer raised a support ticket. " +
                "Title: " + Title + ". " +
                "Description: " + description + ". " +
                "Write a short, professional reply in 2 sentences.";
        List<ChatMessage> messages = List.of(UserMessage.from(prompt));
        ChatRequest request = ChatRequest.builder()
                .messages(messages)
                .build();
        return model.chat(request).aiMessage().text();
    }
}
