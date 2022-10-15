package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapperImpl implements CommentMapper {
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Comment toComment(CommentDto commentDto, Long userId, Long itemId) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(userService.findUserById(userId))
                .item(itemService.findItemById(itemId, null))
                .created(LocalDateTime.now())
                .build();
    }

    @Override
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    @Override
    public List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments.stream().map(this::toCommentDto).collect(Collectors.toList());
    }
}
