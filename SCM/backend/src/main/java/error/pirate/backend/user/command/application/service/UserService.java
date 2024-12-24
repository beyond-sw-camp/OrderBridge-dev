package error.pirate.backend.user.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public UserDetails loadUserByUsername(String userEmployeeNo) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmployeeNo(userEmployeeNo).orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        List<GrantedAuthority> authList = new ArrayList<>();

        // user가 가지고 있는 권한을 권한 리스트에 추가 (권한이 있을까?)
        authList.add(new SimpleGrantedAuthority(user.getUserRole().getValue()));

        return new org.springframework.security.core.userdetails.User(user.getUserEmployeeNo(), user.getUserPwd(), authList); // Id, Pwd, 권한을 Security에서 관리하는 User 객체에 넣는다.
    }
}
