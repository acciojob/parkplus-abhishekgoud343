package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
       Optional<Reservation> reservationOptional = reservationRepository2.findById(reservationId);

        Payment payment = new Payment();
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();

            if (mode != null) {
                if (mode.equalsIgnoreCase("cash"))
                    payment.setPaymentMode(PaymentMode.CASH);
                else if (mode.equalsIgnoreCase("card"))
                    payment.setPaymentMode(PaymentMode.CARD);
                else if (mode.equalsIgnoreCase("upi"))
                    payment.setPaymentMode(PaymentMode.UPI);
                else
                    return payment;
            }

            if (amountSent >= reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours())
                payment.setPaymentCompleted(true);
            else
                return payment;

            payment.setReservation(reservation);
            reservation.setPayment(payment);

            reservationRepository2.save(reservation);
        }

        return payment;
    }
}