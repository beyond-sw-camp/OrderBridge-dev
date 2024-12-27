package error.pirate.backend.chatbot.service;

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

    public String loadSystemInstruction(){
        try{
            ClassPathResource resource = new ClassPathResource("system_instruction.txt");
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: system_instruction.txt", e);
        }
    }

    private final GeminiInterface geminiInterface;

    private ChatbotResponse getCompletion(ChatbotRequest request) {
        return geminiInterface.getCompletion(GEMINI_FLASH, request);
    }

    public String getCompletion(String text) {
        String systemInstruction = loadSystemInstruction();
        log.info("systemInstruction {}", systemInstruction);
        ChatbotRequest geminiRequest = new ChatbotRequest(text, systemInstruction);
        ChatbotResponse response = getCompletion(geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst().flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(ChatbotResponse.TextPart::getText))
                .orElse(null);
    }

}
