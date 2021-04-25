package lt.liutikas.service;

import com.google.common.hash.Hashing;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.LoginRequestDto;
import lt.liutikas.dto.LoginResponseDto;
import lt.liutikas.dto.SignUpRequestDto;
import lt.liutikas.dto.SignUpResponseDto;
import lt.liutikas.model.User;
import lt.liutikas.repository.UserRepository;
import lt.liutikas.utility.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public UserService(UserRepository userRepository, TokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());

        if (optionalUser.isEmpty()) {
            String message = String.format("User not found {email: '%s'}", loginRequestDto.getEmail());
            LOG.error(message);
            throw new NotFoundException(message);
        }

        User user = optionalUser.get();
        if (!passwordsMatch(loginRequestDto, user)) {
            String message = "Passwords don't match";
            LOG.error(message);
            throw new BadRequestException(message);
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        loginResponseDto.setToken(tokenUtil.getToken(
                user.getUserId(),
                user.getUserType()));

        return loginResponseDto;
    }

    private boolean passwordsMatch(LoginRequestDto loginRequestDto, User user) {
        return user.getPasswordHash().equals(getHash(loginRequestDto.getPassword()));
    }

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        Optional<User> existingUser = userRepository.findByEmail(signUpRequestDto.getEmail());

        if (existingUser.isPresent()) {
            String message = String.format("Property already taken {email: '%s'}", signUpRequestDto.getEmail());
            LOG.error(message);
            throw new BadRequestException(message);
        }

        User user = new User();
        user.setEmail(signUpRequestDto.getEmail());
        user.setPasswordHash(getHash(signUpRequestDto.getPassword()));
        user.setUserType(signUpRequestDto.getUserType());

        user = userRepository.save(user);

        LOG.info(String.format("New user signed up {userId: %d}", user.getUserId()));

        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        signUpResponseDto.setUserId(user.getUserId());

        return signUpResponseDto;
    }

    private String getHash(String password) {
        return Hashing.sha256()
                .hashUnencodedChars(password)
                .toString();
    }

}
