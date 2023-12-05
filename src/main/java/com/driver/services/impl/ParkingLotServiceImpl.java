package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();

        Optional<ParkingLot> parkingLot = parkingLotRepository1.findById(parkingLotId);
        if (!parkingLot.isPresent())
            return null;
        spot.setParkingLot(parkingLot.get());

        if (numberOfWheels == 2)
            spot.setSpotType(SpotType.TWO_WHEELER);
        else if (numberOfWheels == 4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else if (numberOfWheels > 4)
            spot.setSpotType(SpotType.OTHERS);
        else
            return null;

        spot.setPricePerHour(pricePerHour);

        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<Spot> spotOptional = spotRepository1.findById(spotId);
        if (!spotOptional.isPresent())
            return null;
        Spot spot = spotOptional.get();

//        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(spotId);
//        if (!parkingLotOptional.isPresent())
//            return null;
//        ParkingLot parkingLot = parkingLotOptional.get();

        spot.setPricePerHour(pricePerHour);

        return spotRepository1.save(spot);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        spotRepository1.deleteAll(parkingLot.getSpotList());
        parkingLotRepository1.delete(parkingLot);
    }
}