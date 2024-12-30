package error.pirate.backend.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import error.pirate.backend.chatbot.dto.ChatbotMessageResponse;
import error.pirate.backend.chatbot.dto.ChatbotRequest;
import error.pirate.backend.chatbot.dto.ChatbotResponse;
import error.pirate.backend.config.GeminiInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {
    public static final String GEMINI_FLASH = "gemini-1.5-flash-latest";
    private final GeminiInterface geminiInterface;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String loadSystemInstruction(){
        try{
            ClassPathResource resource = new ClassPathResource("system_instruction.txt");
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: system_instruction.txt", e);
        }
    }

    private String extractResponseField(String rawMessage) {
        try {
            // JSON 파싱
            JsonNode rootNode = objectMapper.readTree(rawMessage);
            return rootNode.path("message").asText(null); // "message" 필드 값 추출
        } catch (Exception e) {
            log.error("JSON Parsing Error: {}", e.getMessage());
            return rawMessage; // 실패 시 원본 메시지 반환
        }
    }

    private ChatbotResponse getCompletion(ChatbotRequest request) {
        return geminiInterface.getCompletion(GEMINI_FLASH, request);
    }

    public ChatbotMessageResponse getCompletion(String Text) {
        String systemInstruction = loadSystemInstruction();

        ChatbotRequest geminiRequest = new ChatbotRequest(Text, systemInstruction);
        ChatbotResponse response = getCompletion(geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst()
                .map(candidate -> {
                    String role = candidate.getContent().getRole(); // role 가져오기
                    String rawMessage = candidate.getContent().getParts()
                            .stream()
                            .findFirst()
                            .map(ChatbotResponse.TextPart::getText)
                            .orElse(null); // 메시지 가져오기

                    // JSON 메시지에서 "message" 필드 추출
                    String message = extractResponseField(rawMessage);
                    log.info("message {}", message);
                    return new ChatbotMessageResponse(role, message); // role과 메시지를 포함한 DTO 생성
                })
                .orElse(null);
    }

}
