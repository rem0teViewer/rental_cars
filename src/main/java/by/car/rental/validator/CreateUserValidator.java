package by.car.rental.validator;

import by.car.rental.domain.UsersRole;
import by.car.rental.dto.CreateUserDto;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        ValidationResult validationResult = new ValidationResult();
        if (UsersRole.find(object.getRole()).isEmpty()) {
            validationResult.add(Error.of("invalid.role", "Role is invalid"));
        }
        return validationResult;
    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
