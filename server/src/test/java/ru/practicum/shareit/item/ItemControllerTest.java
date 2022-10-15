package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private MockMvc mvc;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Item item;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Санки")
                .description("Санки детские с ручкой")
                .available(true)
                .requestId(3L)
                .build();
        commentDto = CommentDto.builder()
                .id(5L)
                .text("Замечательные санки")
                .authorName("Nina")
                .created(LocalDateTime.now())
                .build();
        item = Item.builder()
                .id(2L)
                .name("Санки")
                .description("Санки детские с ручкой")
                .available(true)
                .owner(User.builder()
                        .id(22L)
                        .name("Kristina")
                        .email("Kris1990@gmail.com")
                        .build())
                .lastBooking(Booking.builder()
                        .id(77L)
                        .booker(User.builder()
                                .id(88L)
                                .build())
                        .build())
                .nextBooking(Booking.builder()
                        .id(101L)
                        .booker(User.builder()
                                .id(99L)
                                .build())
                        .build())
                .itemRequest(ItemRequest.builder()
                        .id(125L)
                        .build())
                .build();
    }

    @Test
    void createComment() throws Exception {
        when(commentMapper.toCommentDto(
                itemService.createComment(
                        commentMapper.toComment(any(), anyLong(), anyLong()))))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));

    }

    @Test
    void create() throws Exception {
        when(itemMapper.toItemDto(itemService.create(
                itemMapper.toItem(any(), anyLong())))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

    }

    @Test
    void update() throws Exception {
        when(itemMapper.toItem(any(), anyLong())).thenReturn(item);

        when(itemMapper.toItemDto(itemService.update(item))).thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void findItem() throws Exception {
        when(itemMapper.toItemDto(
                itemService.findItemById(anyLong(), anyLong())))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

    }

    @Test
    void findAll() throws Exception {
        when(itemMapper.toItemDto(itemService.findAll(anyInt(), anyInt(), anyLong())))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
    }

}