package error.pirate.backend.chatbot.controller;

import error.pirate.backend.chatbot.dto.ChatbotMessageResponse;
import error.pirate.backend.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chatbot")
@Tag(name = "챗봇", description = "챗봇 API")
@Slf4j
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping
    public ResponseEntity<ChatbotMessageResponse> chatbot(@RequestParam String message) {
        try {
            log.info("message : {}" , message);
            return ResponseEntity.ok(chatbotService.getCompletion(message));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body((ChatbotMessageResponse) Collections.singletonMap("error", e.getMessage()));
        }
    }
}