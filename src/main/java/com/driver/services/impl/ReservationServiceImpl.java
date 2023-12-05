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
        User user = userRepository3.findById(userId).orElseThrow(() -> new ReservationFailedException("Invalid user id"));
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElseThrow(() -> new ReservationFailedException("Invalid parking lot Id"));
        Spot spot = null;

        SpotType spotType;
        if (numberOfWheels == 2)
            spotType = SpotType.TWO_WHEELER;
        else if (numberOfWheels == 4)
            spotType = SpotType.FOUR_WHEELER;
        else if (numberOfWheels > 4)
            spotType = SpotType.OTHERS;
        else
            throw new ReservationFailedException("Invalid vehicle");

        List<Spot> spotList = parkingLot.getSpotList();
        spotList.sort(Comparator.comparingInt(Spot::getPricePerHour));
        for (Spot sp : spotList)
            if (sp.getSpotType() == spotType && !sp.getOccupied()) {
                spot = sp;
                spot.setOccupied(true);
                break;
            }
        if (spot == null)
            throw new ReservationFailedException("Suitable spot not found");

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);
        reservation.setPayment(new Payment());
        reservation.setBill(spot.getPricePerHour() * timeInHours);

        return reservationRepository3.save(reservation);
    }
}