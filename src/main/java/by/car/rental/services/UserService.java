package by.car.rental.services;

import by.car.rental.dao.UserDao;
import by.car.rental.domain.User;
import by.car.rental.dto.CreateUserDto;
import by.car.rental.dto.UserDto;
import by.car.rental.exception.ValidationException;
import by.car.rental.mapper.CreateUserMapper;
import by.car.rental.mapper.UserMapper;
import by.car.rental.validator.CreateUserValidator;
import by.car.rental.validator.ValidationResult;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserService {

    private static final UserService INSTANCE = new UserService();

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::mapFrom);
    }

    public Long create(CreateUserDto userDto) {
        ValidationResult validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User user = createUserMapper.mapFrom(userDto);
        userDao.save(user);
        return user.getId();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
