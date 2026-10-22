package by.ipprpo.rental;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.BicycleStatus;
import by.ipprpo.rental.model.Customer;
import by.ipprpo.rental.model.Rental;
import by.ipprpo.rental.repository.InMemoryRentalRepository;
import by.ipprpo.rental.repository.RentalRepository;
import by.ipprpo.rental.service.RentalService;

public class Main {
    public static void main(String[] args) {
        RentalRepository repository = new InMemoryRentalRepository();
        RentalService rentalService = new RentalService(repository);

        Customer customer = new Customer(1, "Иван");
        Bicycle bicycle1 = new Bicycle(101, "Stels Navigator", BicycleStatus.AVAILABLE);
        Bicycle bicycle2 = new Bicycle(102, "Trek Marlin", BicycleStatus.MAINTENANCE);

        try {
            Rental rental = rentalService.createRental(customer, bicycle1);
            System.out.println("Аренда создана: " + rental);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            rentalService.createRental(new Customer(2, "Петр"), bicycle1);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            rentalService.createRental(customer, bicycle2);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        Rental activeRental = repository.findActiveByBicycle(bicycle1).orElseThrow();
        rentalService.returnBicycle(activeRental);
        System.out.println("Велосипед возвращён, статус: " + bicycle1.getStatus());
    }
}