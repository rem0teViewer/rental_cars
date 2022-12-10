package by.car.rental.domain;

import java.util.Arrays;
import java.util.Optional;

public enum UsersRole {
    ADMIN,
    CLIENT;

    public static Optional<UsersRole> find(String role) {
        return Arrays.stream(values())
                .filter(usersRole -> usersRole.name().equals(role))
                .findFirst();
    }
}
