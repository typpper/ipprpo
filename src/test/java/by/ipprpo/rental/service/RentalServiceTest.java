package by.ipprpo.rental.service;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.BicycleStatus;
import by.ipprpo.rental.model.Customer;
import by.ipprpo.rental.model.Rental;
import by.ipprpo.rental.repository.InMemoryRentalRepository;
import by.ipprpo.rental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private RentalRepository repository;
    private RentalService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRentalRepository();
        service = new RentalService(repository);
    }

    @Test
    void testCreateRentalSuccess() {
        Customer customer = new Customer(1, "Иван");
        Bicycle bicycle = new Bicycle(101, "Stels", BicycleStatus.AVAILABLE);

        Rental rental = service.createRental(customer, bicycle);

        assertNotNull(rental);
        assertEquals(customer, rental.getCustomer());
        assertEquals(bicycle, rental.getBicycle());
        assertTrue(rental.isActive());
        assertEquals(BicycleStatus.MAINTENANCE, bicycle.getStatus());
        assertTrue(repository.findActiveByBicycle(bicycle).isPresent());
    }

    @Test
    void testCreateRentalAlreadyRented() {
        Customer customer1 = new Customer(1, "Иван");
        Bicycle bicycle = new Bicycle(101, "Stels", BicycleStatus.AVAILABLE);

        service.createRental(customer1, bicycle);

        Customer customer2 = new Customer(2, "Петр");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.createRental(customer2, bicycle));

        assertEquals("Велосипед уже арендован", exception.getMessage());
    }

    @Test
    void testCreateRentalOnMaintenance() {
        Customer customer = new Customer(1, "Иван");
        Bicycle bicycle = new Bicycle(102, "Trek", BicycleStatus.MAINTENANCE);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.createRental(customer, bicycle));

        assertEquals("Велосипед находится на обслуживании", exception.getMessage());
    }
        @Test
    void testReturnBicycleSuccess() {
        Customer customer = new Customer(1, "Иван");
        Bicycle bicycle = new Bicycle(101, "Stels", BicycleStatus.AVAILABLE);

        Rental rental = service.createRental(customer, bicycle);
        assertEquals(BicycleStatus.RENTED, bicycle.getStatus());

        service.returnBicycle(rental);

        assertFalse(rental.isActive());
        assertNotNull(rental.getEndTime());
        assertEquals(BicycleStatus.AVAILABLE, bicycle.getStatus());
        assertTrue(repository.findActiveByBicycle(bicycle).isEmpty());
    }

    @Test
    void testReturnBicycleAlreadyReturned() {
        Customer customer = new Customer(1, "Иван");
        Bicycle bicycle = new Bicycle(101, "Stels", BicycleStatus.AVAILABLE);

        Rental rental = service.createRental(customer, bicycle);
        service.returnBicycle(rental);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.returnBicycle(rental));

        assertEquals("Аренда уже завершена", exception.getMessage());
    }
}