package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;

    @Autowired
    SpotRepository spotRepository3;

//    @Autowired
//    ReservationRepository reservationRepository3;

    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user = userRepository3.findById(userId).orElseThrow(() -> new Exception("Cannot make reservation"));
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElseThrow(() -> new Exception("Cannot maker reservation"));

        List<Spot> potentialSpotList = new ArrayList<>();
        for (Spot sp : parkingLot.getSpotList())
            if (!sp.getOccupied()) {
                int wheels;

                if (sp.getSpotType() == SpotType.TWO_WHEELER)
                    wheels = 2;
                else if (sp.getSpotType() == SpotType.FOUR_WHEELER)
                    wheels = 4;
                else
                    wheels = Integer.MAX_VALUE;

                if (wheels >= numberOfWheels) {
                    potentialSpotList.add(sp);
                }
            }
        if (potentialSpotList.isEmpty())
            throw new Exception("Cannot maker reservation");

        potentialSpotList.sort(Comparator.comparingInt(Spot::getPricePerHour));

        Spot spot = potentialSpotList.get(0);
        spot.setOccupied(true);

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(spot);

        user.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(spot);

        return reservation;
    }
}