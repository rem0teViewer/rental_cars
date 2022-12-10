package by.car.rental.dto;

import by.car.rental.domain.UsersRole;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    UsersRole role;
}
