package by.car.rental.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
