package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        if (!parkingLotOptional.isPresent())
            return null;
        ParkingLot parkingLot = parkingLotOptional.get();

        Spot spot = new Spot();

        if (numberOfWheels <= 2)
            spot.setSpotType(SpotType.TWO_WHEELER);
        else if (numberOfWheels == 4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else if (numberOfWheels > 4)
            spot.setSpotType(SpotType.OTHERS);
        else
            return null;

        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);

        parkingLot.getSpotList().add(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<ParkingLot> optionalParkingLot=parkingLotRepository1.findById(parkingLotId);
        if(optionalParkingLot.isPresent()){
            List<Spot> spotList=optionalParkingLot.get().getSpotList();
            Optional<Spot> optionalSpot=spotRepository1.findById(spotId);
            if(optionalSpot.isPresent()){
                if(spotList.contains(optionalSpot.get())){
                    Spot spot=optionalSpot.get();
                    spot.setOccupied(false);
                    spot.setParkingLot(optionalParkingLot.get());
                    spot.setPricePerHour(pricePerHour);
                    spotRepository1.save(spot);

                    return spot;
                }
            }
        }

        return new Spot();
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
//        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
//        if (!parkingLotOptional.isPresent())
//            return;
//        ParkingLot parkingLot = parkingLotOptional.get();
//
//        spotRepository1.deleteAll(parkingLot.getSpotList());
        parkingLotRepository1.deleteById(parkingLotId);
    }
}