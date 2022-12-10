package by.car.rental.mapper;

import by.car.rental.domain.User;
import by.car.rental.domain.UsersRole;
import by.car.rental.dto.CreateUserDto;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserMapper implements Mapper<CreateUserDto, User> {

    private static final CreateUserMapper INSTANCE = new CreateUserMapper();

    @Override
    public User mapFrom(CreateUserDto object) {
        return User.builder()
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .email(object.getEmail())
                .password(object.getPassword())
                .role(UsersRole.valueOf(object.getRole()))
                .build();
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
}
