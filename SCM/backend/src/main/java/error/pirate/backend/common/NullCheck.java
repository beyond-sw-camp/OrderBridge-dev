package error.pirate.backend.common;

import org.springframework.stereotype.Component;

@Component
public class NullCheck {
    public static boolean nullOrZeroCheck(Long value) {
        return value != null && value != 0L;
    }

    public static boolean nullOrZeroCheck(Integer value) {
        return value != null && value != 0;
    }

    public static boolean nullCheck(Object value) {
        return value != null;
    }
}
