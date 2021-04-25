package lt.liutikas.service;

import com.google.common.hash.Hashing;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateUserRequestDto;
import lt.liutikas.dto.CreateUserResponseDto;
import lt.liutikas.dto.GetTokenDto;
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

    public GetTokenDto getToken(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            String message = String.format("User not found {email: '%s'}", email);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        User user = optionalUser.get();
        if (!passwordsMatch(user, password)) {
            String message = "Passwords don't match";
            LOG.error(message);
            throw new BadRequestException(message);
        }

        GetTokenDto getTokenDto = new GetTokenDto();

        getTokenDto.setToken(tokenUtil.getToken(
                user.getUserId(),
                user.getUserType()));

        return getTokenDto;
    }

    private boolean passwordsMatch(User user, String password) {
        return user.getPasswordHash().equals(getHash(password));
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {

        Optional<User> existingUser = userRepository.findByEmail(createUserRequestDto.getEmail());

        if (existingUser.isPresent()) {
            String message = String.format("Property already taken {email: '%s'}", createUserRequestDto.getEmail());
            LOG.error(message);
            throw new BadRequestException(message);
        }

        User user = new User();
        user.setEmail(createUserRequestDto.getEmail());
        user.setPasswordHash(getHash(createUserRequestDto.getPassword()));
        user.setUserType(createUserRequestDto.getUserType());

        user = userRepository.save(user);

        LOG.info(String.format("New user signed up {userId: %d}", user.getUserId()));

        CreateUserResponseDto createUserResponseDto = new CreateUserResponseDto();
        createUserResponseDto.setUserId(user.getUserId());

        return createUserResponseDto;
    }

    private String getHash(String password) {
        return Hashing.sha256()
                .hashUnencodedChars(password)
                .toString();
    }

}
