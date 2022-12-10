package by.car.rental.mapper;

import by.car.rental.domain.User;
import by.car.rental.dto.UserDto;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserMapper implements Mapper<User, UserDto> {

    private static final UserMapper INSTANCE = new UserMapper();

    @Override
    public UserDto mapFrom(User object) {
        return UserDto.builder()
                .id(object.getId())
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .email(object.getEmail())
                .role(object.getRole())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
