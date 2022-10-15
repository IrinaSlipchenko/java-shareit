package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingOutputDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private ShortUserDto booker;

    private ShortItemDto item;

}
