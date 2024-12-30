package error.pirate.backend.config;

import error.pirate.backend.chatbot.dto.ChatbotRequest;
import error.pirate.backend.chatbot.dto.ChatbotResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {

    @PostExchange("{model}:generateContent")
    ChatbotResponse getCompletion(
            @PathVariable String model,
            @RequestBody ChatbotRequest request
    );
}
