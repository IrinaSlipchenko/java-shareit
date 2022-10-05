package ru.practicum.shareit.requests.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInputDto {

    @NotBlank
    private String description;
}
