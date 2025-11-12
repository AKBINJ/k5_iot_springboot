
package com.example.k5_iot_springboot.dto.Reservation;

import com.example.k5_iot_springboot.entity.Reservation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationResponseDto {
    private Long id;
    private Long truckId;
    private Long userId;
    private LocalDate date;
    private String timeSlot;
    private String status;

    public static ReservationResponseDto fromEntity(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.id = reservation.getId();
        dto.truckId = reservation.getTruck().getId();
        dto.userId = reservation.getUser().getId();
        dto.date = reservation.getDate();
        dto.timeSlot = reservation.getTimeSlot();
        dto.status = reservation.getStatus();
        return dto;
    }
}