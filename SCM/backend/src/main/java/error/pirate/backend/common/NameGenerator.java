package error.pirate.backend.common;

import error.pirate.backend.common.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NameGenerator {

    private final CommonMapper commonMapper;

    /**
     * Entity.class를 입력하면 제목을 출력하는 메소드 입니다.<br>
     * 제목의 형식은 "0000/00/00 - 0" 이고, 등록일 기준으로 생성합니다.
     *
     * @param domain Entity.class
     * @return 제목
     */
    public String nameGenerator(Class domain) {
        return commonMapper.nameGenerator(pascalToSnake(domain.getSimpleName()));
    }

    private String pascalToSnake(String input) {
        StringBuilder output = new StringBuilder();
        output.append(Character.toLowerCase(input.charAt(0)));

        for (int i = 1; i < input.length(); i++) {
            if (65 <= input.charAt(i) && input.charAt(i) <= 90) {
                output.append('_').append(Character.toLowerCase(input.charAt(i)));
            } else {
                output.append(input.charAt(i));
            }
        }

        return output.toString();
    }
}
