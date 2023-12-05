package com.driver.services.impl;

import com.driver.exceptions.ReservationFailedException;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws ReservationFailedException {
        Optional<User> userOptional = userRepository3.findById(userId); //.orElseThrow(() -> new ReservationFailedException("Invalid user id"));
        if (!userOptional.isPresent())
            return null;
        User user = userOptional.get();

        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElseThrow(() -> new ReservationFailedException("Cannot make reservation"));
//        if (!parkingLotOptional.isPresent())
////            return null;
//        ParkingLot parkingLot = parkingLotOptional.get();

        Spot spot = null;

        SpotType spotType;
        if (numberOfWheels == 2)
            spotType = SpotType.TWO_WHEELER;
        else if (numberOfWheels == 4)
            spotType = SpotType.FOUR_WHEELER;
        else if (numberOfWheels > 4)
            spotType = SpotType.OTHERS;
        else
            return null;
//            throw new ReservationFailedException("Invalid vehicle");

        List<Spot> spotList = parkingLot.getSpotList();
        spotList.sort(Comparator.comparingInt(Spot::getPricePerHour));
        for (Spot sp : spotList)
            if (sp.getSpotType() == spotType && !sp.getOccupied()) {
                spot = sp;
                spot.setOccupied(true);
                break;
            }
        if (spot == null)
            return null;
//            throw new ReservationFailedException("Suitable spot not found");

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);
        reservation.setPayment(new Payment());
        reservation.setBill(spot.getPricePerHour() * timeInHours);

        return reservationRepository3.save(reservation);
    }
}