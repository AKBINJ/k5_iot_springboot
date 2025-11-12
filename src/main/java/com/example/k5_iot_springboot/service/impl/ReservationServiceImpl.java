
package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.Reservation.ReservationResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.Reservation;
import com.example.k5_iot_springboot.repository.ReservationRepository;
import com.example.k5_iot_springboot.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Override
    public ResponseDto<List<ReservationResponseDto>> getReservationsByTruck(Long truckId) {
        List<Reservation> reservations = reservationRepository.findByTruckId(truckId);

        List<ReservationResponseDto> dtos = reservations.stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("해당 truckId의 예약 목록을 성공적으로 응답하였습니다.", dtos);
    }

    @Override
    public ResponseDto<ReservationResponseDto> getReservation(Long truckId, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndTruckId(reservationId, truckId)
                .orElseThrow(() -> new IllegalArgumentException("해당 트럭에 대한 예약을 찾을 수 없습니다."));

        ReservationResponseDto dto = ReservationResponseDto.fromEntity(reservation);
        return ResponseDto.setSuccess("해당 truckId의 reservationId 예약을 성공적으로 응답하였습니다.", dto);
    }
}
