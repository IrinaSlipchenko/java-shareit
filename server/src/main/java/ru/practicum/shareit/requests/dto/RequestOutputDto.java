package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestOutputDto {
    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
