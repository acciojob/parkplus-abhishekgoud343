package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;

    @Autowired
    SpotRepository spotRepository3;

    @Autowired
    ReservationRepository reservationRepository3;

    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> userOptional = userRepository3.findById(userId);
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository3.findById(parkingLotId);

        if (!userOptional.isPresent() || !parkingLotOptional.isPresent()) return null;

        User user = userOptional.get();
        ParkingLot parkingLot = parkingLotOptional.get();

        Reservation reservation = new Reservation();

        Spot spot = null;
        int cost = Integer.MAX_VALUE;
        for (Spot sp : parkingLot.getSpotList())
            if (!sp.getOccupied()) {
                int wheels = 0;

                if (sp.getSpotType() == SpotType.TWO_WHEELER)
                    wheels = 2;
                else if (sp.getSpotType() == SpotType.FOUR_WHEELER)
                    wheels = 4;
                else
                    wheels = Integer.MAX_VALUE;

                if (wheels >= numberOfWheels) {
                    int currentCost = sp.getPricePerHour() * timeInHours;

                    if (currentCost < cost) {
                        cost = currentCost;
                        spot = sp;
                    }
                }
            }
        if (spot == null) return reservation;
        spot.setOccupied(true);
        spot.getReservationList().add(reservation);

        Payment payment = new Payment();
        payment.setReservation(reservation);

        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(spot);
        reservation.setPayment(payment);

        userRepository3.save(user);

        return reservation;
    }
}