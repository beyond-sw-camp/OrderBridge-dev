package error.pirate.backend.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChatbotRequest {

    private List<Content> contents;
    private SystemInstruction systemInstruction;    // 모델에 초기 지침을 제공
    private GenerationConfig generationConfig;  // 생성 설정을 포함하여 모델의 응답 생성 방식을 조정

    public ChatbotRequest(String text, String systemText) {
        Part part = new TextPart(text);
        Content content = new Content(Collections.singletonList(part));
        this.contents = Arrays.asList(content);

        // Add system instruction
        this.systemInstruction = new SystemInstruction(systemText);

        // Add default generation configuration
        this.generationConfig = new GenerationConfig(1.0, 40, 0.95, 8192, "application/json");
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private List<Part> parts;
    }

    interface Part {}

    @Getter
    @AllArgsConstructor
    private static class TextPart implements Part {
        public String text;
    }

    @Getter
    @AllArgsConstructor
    private static class InlineDataPart implements Part {
        public InlineData inlineData;
    }

    @Getter
    @AllArgsConstructor
    public static class InlineData {
        private String mimeType;
        private String data;
    }

    @Getter
    @AllArgsConstructor
    public static class SystemInstruction {
        private String role = "user";
        private List<TextPart> parts;

        public SystemInstruction(String systemText) {
            this.parts = Collections.singletonList(new TextPart(systemText));
        }
    }

    @Getter
    @AllArgsConstructor
    public static class GenerationConfig {
        private double temperature;
        private int topK;
        private double topP;
        private int maxOutputTokens;
        private String responseMimeType;
    }
}
