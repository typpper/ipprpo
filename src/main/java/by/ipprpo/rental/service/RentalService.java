package by.ipprpo.rental.service;

import by.ipprpo.rental.model.Bicycle;
import by.ipprpo.rental.model.BicycleStatus;
import by.ipprpo.rental.model.Customer;
import by.ipprpo.rental.model.Rental;
import by.ipprpo.rental.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RentalService {
    private static final Logger LOG = LoggerFactory.getLogger(RentalService.class);
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental createRental(Customer customer, Bicycle bicycle) {

        LOG.info("Запрос на создание аренды: клиент={}, велосипед={}", customer.getName(), bicycle.getModel());

        if (bicycle.getStatus() == BicycleStatus.RENTED) {
            LOG.warn("Отказ: велосипед {} уже арендован", bicycle.getModel());
            throw new IllegalStateException("Велосипед уже арендован");
        }
        if (bicycle.getStatus() == BicycleStatus.MAINTENANCE) {
            LOG.warn("Отказ: велосипед {} находится на обслуживании", bicycle.getModel());
            throw new IllegalStateException("Велосипед находится на обслуживании");
        }

        Optional<Rental> activeRental = rentalRepository.findActiveByBicycle(bicycle);
        if (activeRental.isPresent()) {
            LOG.warn("Отказ: для велосипеда {} уже есть активная аренда", bicycle.getModel());
            throw new IllegalStateException("Для велосипеда уже существует активная аренда");
        }

        try {
            int rentalId = rentalRepository.nextId();
            Rental rental = new Rental(rentalId, customer, bicycle, LocalDateTime.now());
            bicycle.setStatus(BicycleStatus.RENTED);
            rentalRepository.save(rental);

            LOG.info("Аренда успешно создана: id={}, клиент={}, велосипед={}",
                    rentalId, customer.getName(), bicycle.getModel());
            return rental;
        } catch (Exception e) {
            LOG.error("Ошибка при сохранении аренды для велосипеда {}", bicycle.getModel(), e);
            throw e;
        }
    }

    public void returnBicycle(Rental rental) {
        LOG.info("Запрос на возврат велосипеда: id={}", rental.getId());

        if (!rental.isActive()) {
            LOG.warn("Отказ: аренда {} уже завершена", rental.getId());
            throw new IllegalStateException("Аренда уже завершена");
        }
        rental.setEndTime(LocalDateTime.now());
        rental.getBicycle().setStatus(BicycleStatus.AVAILABLE);
        rentalRepository.save(rental);

        LOG.info("Велосипед успешно возвращён: id={}, модель={}",
                rental.getId(), rental.getBicycle().getModel());
    }
}