package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).orElseThrow(() -> new Exception("Invalid reservation id"));

        if (amountSent < reservation.getBill())
            throw new Exception("Insufficient amount");

        Payment payment = reservation.getPayment();

        if (mode.equalsIgnoreCase("cash"))
            payment.setPaymentMode(PaymentMode.CASH);
        else if (mode.equalsIgnoreCase("card"))
            payment.setPaymentMode(PaymentMode.CARD);
        else if (mode.equalsIgnoreCase("upi"))
            payment.setPaymentMode(PaymentMode.UPI);
        else
            throw new Exception("Payment mode not detected");

        payment.setPaymentCompleted(true);

        return paymentRepository2.save(payment);
    }
}