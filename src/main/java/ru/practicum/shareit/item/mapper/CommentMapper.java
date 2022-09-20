package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentMapper {

    Comment toComment(CommentDto commentDto, Long userId, Long itemId);

    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDto(List<Comment> comments);
}
