package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingController controller;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private LocalDateTime start;
    private LocalDateTime end;
    private User user;
    private BookingOutputDto bookingOutputDto;
    private BookingInputDto bookingInputDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        start = LocalDateTime.now().plusMinutes(1);
        end = LocalDateTime.now().plusDays(1);

        user = User.builder()
                .id(1L)
                .name("Test name")
                .build();

        bookingOutputDto = BookingOutputDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(new ShortUserDto(1L))
                .item(new ShortItemDto(1L, "Самокат"))
                .build();

        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();
    }

    @Test
    void create() throws Exception {
        when(bookingMapper.toBookingDto(bookingService.create(
                bookingMapper.toBooking(bookingInputDto, 1L)))).thenReturn(bookingOutputDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().toString())));

    }
}