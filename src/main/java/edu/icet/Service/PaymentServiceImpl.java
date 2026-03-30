package edu.icet.Service;

import edu.icet.Model.Dto.PaymentDto;
import edu.icet.Model.Dto.PaymentRequestDto;
import edu.icet.Model.Entity.Payment;
import edu.icet.Model.Entity.Rental;
import edu.icet.Repository.CarDetailsRepository;
import edu.icet.Repository.PaymentRepository;
import edu.icet.Repository.RentalRepository;
import edu.icet.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final CarDetailsRepository carDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentRequestDto paymentRequestDto) {
        // Verify rental exists
        Rental rental = rentalRepository.findById(paymentRequestDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Rental not found with booking id: " + paymentRequestDto.getBookingId()));

        // Check if payment already exists for this booking
        if (paymentRepository.existsByBookingId(paymentRequestDto.getBookingId())) {
            throw new RuntimeException("Payment already exists for booking id: " + paymentRequestDto.getBookingId());
        }

        // Validate amount matches rental total price
        if (!paymentRequestDto.getAmount().equals(rental.getTotalPrice())) {
            throw new RuntimeException("Payment amount does not match rental total price");
        }

        // Create payment
        Payment payment = new Payment();
        payment.setBookingId(paymentRequestDto.getBookingId());
        payment.setPaymentDate(paymentRequestDto.getPaymentDate());
        payment.setAmount(paymentRequestDto.getAmount());

        Payment savedPayment = paymentRepository.save(payment);

        // Update rental status to approved after payment
        rental.setStatus("approved");
        rentalRepository.save(rental);

        // Map to DTO and enrich
        PaymentDto paymentDto = modelMapper.map(savedPayment, PaymentDto.class);
        enrichPaymentDto(paymentDto, savedPayment);

        return paymentDto;
    }


    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        enrichPaymentDto(paymentDto, payment);
        return paymentDto;
    }

    @Override
    public PaymentDto getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking id: " + bookingId));

        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        enrichPaymentDto(paymentDto, payment);
        return paymentDto;
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    enrichPaymentDto(dto, payment);
                    return dto;}).collect(Collectors.toList());

    }

    @Override
    public List<PaymentDto> getPaymentsByDate(LocalDate paymentDate) {
        return paymentRepository.findByPaymentDate(paymentDate).stream().map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    enrichPaymentDto(dto, payment);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findPaymentsBetweenDates(startDate, endDate).stream().map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    enrichPaymentDto(dto, payment);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) {
        Payment existingPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        existingPayment.setPaymentDate(paymentDto.getPaymentDate());
        existingPayment.setAmount(paymentDto.getAmount());

        Payment updatedPayment = paymentRepository.save(existingPayment);

        PaymentDto dto = modelMapper.map(updatedPayment, PaymentDto.class);
        enrichPaymentDto(dto, updatedPayment);
        return dto;
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment not found with id: " + paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }

    @Override
    public Double calculateTotalRevenue() {
        Double revenue = paymentRepository.calculateTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Double calculateRevenueForPeriod(LocalDate startDate, LocalDate endDate) {
        Double revenue = paymentRepository.calculateRevenueForPeriod(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public long getPaymentCountForPeriod(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.countPaymentsForPeriod(startDate, endDate);
    }

    private void enrichPaymentDto(PaymentDto paymentDto, Payment savedPayment) {
        rentalRepository.findById(paymentDto.getBookingId()).ifPresent(rental -> {
            paymentDto.setCarId(rental.getCarId());
            paymentDto.setUserId(rental.getUserId());
            paymentDto.setRentalStatus(rental.getStatus());

            // Fetch car details
            carDetailsRepository.findById(rental.getCarId()).ifPresent(car -> {
                paymentDto.setCarModel(car.getCarModel());
                paymentDto.setCarBrand(car.getBrand());
            });

            // Fetch user details
            userRepository.findById(rental.getUserId()).ifPresent(user -> {
                paymentDto.setUserName(user.getFullName());
                paymentDto.setUserEmail(user.getEmail());
            });
        });
    }

}
