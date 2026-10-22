package by.ipprpo.rental.repository;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    void save(Rental rental);
    Optional<Rental> findActiveByBicycle(Bicycle bicycle);
    List<Rental> findAll();
    int nextId();
}