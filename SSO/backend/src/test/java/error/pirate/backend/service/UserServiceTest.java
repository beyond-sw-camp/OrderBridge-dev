package error.pirate.backend.service;

import error.pirate.backend.dto.CreateUserRequest;
import error.pirate.backend.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest(
                "admin",
                "1234",
                "관리자",
                "admin@user.com"
        );

        userService.createUser(request);

        assertThrows(CustomException.class, () -> {

        });
    }
}