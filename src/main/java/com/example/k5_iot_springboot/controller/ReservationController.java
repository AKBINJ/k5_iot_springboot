package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.dto.Reservation.ReservationResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trucks/{truckId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> getReservationsByTruck(
            @PathVariable Long truckId
    ) {
        ResponseDto<List<ReservationResponseDto>> result = reservationService.getReservationsByTruck(truckId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ResponseDto<ReservationResponseDto>> getReservation(
        @PathVariable Long truckId,
        @PathVariable Long reservationId
    ) {
        ResponseDto<ReservationResponseDto> result = reservationService.getReservation(reservationId, truckId);
        return ResponseEntity.ok(result);
    }
}
