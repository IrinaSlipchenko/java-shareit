package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentMapperImplTest {
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CommentMapperImpl commentMapper;

    private User author;
    private Item item;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    void setUp() {
        LocalDateTime created = LocalDateTime.now().withNano(0);
        author = User.builder()
                .id(1L)
                .name("Nina")
                .email("nina2009@yandex.ru")
                .build();

        item = Item.builder()
                .id(2L)
                .name("Санки")
                .description("Санки детские с ручкой")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .id(5L)
                .text("Замечательные санки")
                .authorName("Nina")
                .created(created)
                .build();

        comment = Comment.builder()
                .id(5L)
                .text("Замечательные санки")
                .author(author)
                .item(item)
                .created(created)
                .build();

    }

    @Test
    void toComment() {
        when(userService.findUserById(anyLong())).thenReturn(author);
        when(itemService.findItemById(item.getId(), null)).thenReturn(item);
        Comment testComment = commentMapper.toComment(commentDto, author.getId(), item.getId());

        assertEquals(commentDto.getText(), testComment.getText());
        assertEquals(author, testComment.getAuthor());
        assertEquals(item, testComment.getItem());
        assertEquals(commentDto.getCreated(), testComment.getCreated().withNano(0));
    }

    @Test
    void toCommentDto() {
        CommentDto testCommentDto = commentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), testCommentDto.getId());
        assertEquals(comment.getText(), testCommentDto.getText());
        assertEquals(comment.getAuthor().getName(), testCommentDto.getAuthorName());
        assertEquals(comment.getCreated(), testCommentDto.getCreated().withNano(0));
    }

}