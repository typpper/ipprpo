package by.ipprpo.rental.repository;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryRentalRepository implements RentalRepository {
    private final List<Rental> rentals = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void save(Rental rental) {
        rentals.removeIf(r -> r.getId() == rental.getId());
        rentals.add(rental);
    }

    @Override
    public Optional<Rental> findActiveByBicycle(Bicycle bicycle) {
        return rentals.stream()
                .filter(r -> r.getBicycle().equals(bicycle) && r.isActive())
                .findFirst();
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public int nextId() {
        return nextId++;
    }
}