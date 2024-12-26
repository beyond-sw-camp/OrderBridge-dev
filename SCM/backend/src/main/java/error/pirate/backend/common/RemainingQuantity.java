package error.pirate.backend.common;

import error.pirate.backend.common.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemainingQuantity {

    private final CommonMapper commonMapper;

    /**
     * 남은 품목을 조회할 Entity.class와 등록 할 Entity.class 와 조회할 seq 를 입력하면 남은 품목들을 보여줌<br>
     * 수량에 해당하는 Integer 리스트 반환 .
     *
     * @param selectDomain Entity.class
     * @param joinDomain Entity.class
     * @param seq long
     * @return 남은 수량 리스트
     */
    public List<Integer> remainingQuantity(Class selectDomain, Class joinDomain, long seq) {
        return commonMapper.remainingQuantity(
                pascalToSnake(selectDomain.getSimpleName()),
                pascalToSnake(joinDomain.getSimpleName()), seq);
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
