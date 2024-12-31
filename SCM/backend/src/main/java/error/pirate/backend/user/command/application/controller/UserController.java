package error.pirate.backend.user.command.application.controller;

import error.pirate.backend.user.command.application.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
@Tag(name = "User", description = "회원")
public class UserController {

    private final UserService userService;

    @PostMapping("/logout")
    public void userLogout() {
        userService.userLogout();
    }
}
