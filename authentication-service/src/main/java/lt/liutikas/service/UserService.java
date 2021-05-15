package lt.liutikas.service;

import com.google.common.hash.Hashing;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateUserRequestDto;
import lt.liutikas.dto.CreateUserResponseDto;
import lt.liutikas.dto.GetTokenRequestDto;
import lt.liutikas.dto.GetTokenResponseDto;
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

    private final TokenUtil tokenUtil;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, TokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    public GetTokenResponseDto getToken(GetTokenRequestDto getTokenRequestDto) {

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(getTokenRequestDto.getEmail());

        if (optionalUser.isEmpty()) {
            String message = String.format("User not found {email: '%s'}", getTokenRequestDto.getEmail());
            LOG.error(message);
            throw new NotFoundException(message);
        }

        User user = optionalUser.get();
        if (!passwordsMatch(user, getTokenRequestDto.getPassword())) {
            String message = String.format("Incorrect password {userId: %d}", user.getId());
            LOG.error(message);
            throw new BadRequestException(message, "INCORRECT_PASSWORD");
        }

        GetTokenResponseDto getTokenResponseDto = new GetTokenResponseDto();

        getTokenResponseDto.setToken(tokenUtil.getToken(
                user.getId(),
                user.getUserType().toString()
        ));

        return getTokenResponseDto;
    }

    private boolean passwordsMatch(User user, String password) {
        return user.getPasswordHash().equals(getHash(password));
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {

        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(createUserRequestDto.getEmail());

        if (existingUser.isPresent()) {
            String message = String.format("Property already taken {email: '%s'}", createUserRequestDto.getEmail());
            LOG.error(message);
            throw new BadRequestException(message, "EMAIL_TAKEN");
        }

        User user = new User();
        user.setEmail(createUserRequestDto.getEmail());
        user.setPasswordHash(getHash(createUserRequestDto.getPassword()));
        user.setUserType(createUserRequestDto.getUserType());

        user = userRepository.save(user);

        LOG.info(String.format("New user signed up {userId: %s}", user.getId()));

        CreateUserResponseDto createUserResponseDto = new CreateUserResponseDto();
        createUserResponseDto.setUserId(user.getId());

        return createUserResponseDto;
    }

    private String getHash(String password) {
        return Hashing.sha256()
                .hashUnencodedChars(password)
                .toString();
    }

}
