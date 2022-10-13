package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.dto.RequestOutputDto;
import ru.practicum.shareit.requests.mapper.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private ItemRequestService requestService;
    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private LocalDateTime time;
    private RequestOutputDto requestOutputDto;
    private RequestInputDto requestInputDto;
    private ItemRequest itemRequest;
    private User user;
    private List<ItemDto> items = List.of(ItemDto.builder()
            .id(111L)
            .description("Wonderful boots")
            .available(true)
            .build()
    );

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        requestOutputDto = RequestOutputDto.builder()
                .id(1L)
                .description("Test description")
                .created(time)
                .items(items).build();

        requestInputDto = new RequestInputDto("Test description");

        user = User.builder()
                .id(1L)
                .name("John")
                .email("john.doe@mail.com")
                .build();
    }

    @Test
    void create() throws Exception {
        when(requestMapper.toItemRequestDto(requestService.create(
                requestMapper.toItemRequest(requestInputDto, 1L))))
                .thenReturn(requestOutputDto);


        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestOutputDto.getDescription())))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").exists());

    }

    @Test
    void findRequest() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(user);

        when(requestMapper.toItemRequestDto(requestService.findItemRequestById(anyLong())))
                .thenReturn(requestOutputDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestOutputDto.getDescription())))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void findAll() throws Exception {
        List<RequestOutputDto> requestOutputDtos = List.of(requestOutputDto);
        when(requestMapper.toItemRequestDto(requestService.findAllItemRequest(anyLong())))
                .thenReturn(requestOutputDtos);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        is(requestOutputDtos.stream().findFirst().get().getId()), Long.class))
                .andExpect(jsonPath("$[0].description",
                        is(requestOutputDtos.stream().findFirst().get().getDescription())))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].items").exists());
    }

    @Test
    void findPageOfThisSize() throws Exception {
        List<RequestOutputDto> requestOutputDtos = List.of(requestOutputDto);
        when(requestMapper.toItemRequestDto(requestService.findPage(anyLong(), anyInt(), anyInt())))
                .thenReturn(requestOutputDtos);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        is(requestOutputDtos.stream().findFirst().get().getId()), Long.class))
                .andExpect(jsonPath("$[0].description",
                        is(requestOutputDtos.stream().findFirst().get().getDescription())))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].items").exists());
    }
}