package by.ipprpo.rental.service;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.BicycleStatus;
import by.ipprpo.rental.model.Customer;
import by.ipprpo.rental.model.Rental;
import by.ipprpo.rental.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental createRental(Customer customer, Bicycle bicycle) {
        if (bicycle.getStatus() == BicycleStatus.RENTED) {
            throw new IllegalStateException("Велосипед уже арендован");
        }
        if (bicycle.getStatus() == BicycleStatus.MAINTENANCE) {
            throw new IllegalStateException("Велосипед находится на обслуживании");
        }

        Optional<Rental> activeRental = rentalRepository.findActiveByBicycle(bicycle);
        if (activeRental.isPresent()) {
            throw new IllegalStateException("Для велосипеда уже существует активная аренда");
        }

        int rentalId = rentalRepository.nextId();
        Rental rental = new Rental(rentalId, customer, bicycle, LocalDateTime.now());
        bicycle.setStatus(BicycleStatus.RENTED);
        rentalRepository.save(rental);
        return rental;
    }

    public void returnBicycle(Rental rental) {
        if (!rental.isActive()) {
            throw new IllegalStateException("Аренда уже завершена");
        }
        rental.setEndTime(LocalDateTime.now());
        rental.getBicycle().setStatus(BicycleStatus.AVAILABLE);
        rentalRepository.save(rental);
    }
}