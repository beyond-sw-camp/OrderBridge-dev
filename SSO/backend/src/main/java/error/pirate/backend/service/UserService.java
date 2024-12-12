package error.pirate.backend.service;

import error.pirate.backend.dto.CreateUserRequest;
import error.pirate.backend.entity.User;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);

        List<GrantedAuthority> authList = new ArrayList<>();

        // user가 가지고 있는 권한을 권한 리스트에 추가 (권한이 있을까?)
        // authList.add(new SimpleGrantedAuthority(user.getUserRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPwd(), authList); // Id, Pwd, 권한을 Security에서 관리하는 User 객체에 넣는다.
    }

    public void createUser(CreateUserRequest request) {
        User user = userRepository.findByUserId(request.getUserId());
        if(user != null) {
            throw new CustomException(ErrorCodeType.EXIST_USER);
        }

        request.changePasswordEncoder(request.getUserPwd());

        User newUser = User.builder().userId(request.getUserId())
                .userPwd(request.getUserPwd())
                .userEmail(request.getUserEmail())
                .userName(request.getUserName()).build();

        userRepository.save(newUser);
    }
}
