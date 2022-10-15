package ru.practicum.shareit.booking.dto;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortBookingDto {
    private Long id;

    private Long bookerId;


}
