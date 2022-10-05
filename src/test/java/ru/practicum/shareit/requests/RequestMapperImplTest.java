package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.mapper.RequestMapperImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMapperImplTest {
    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestMapperImpl requestMapper;

    private User requester;
    private RequestInputDto requestInputDto;

    @BeforeEach
    void setUp() {
        requester = User.builder()
                .id(11L)
                .name("Donna")
                .email("donna2003@yandex.ru")
                .build();

        requestInputDto = new RequestInputDto("Мне нужен ковер-самолет, может есть у кого ?");
    }

    @Test
    void toItemRequest() {
        when(userService.findUserById(anyLong())).thenReturn(requester);

        ItemRequest testItemRequest = requestMapper.toItemRequest(requestInputDto, requester.getId());

        assertEquals(requestInputDto.getDescription(), testItemRequest.getDescription());
        assertEquals(requester, testItemRequest.getRequester());
    }

    @Test
    void toItemRequestDto() {
      //  when();
    }

}