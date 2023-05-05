package ru.skypro.homework.service.mapper.impl;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.service.mapper.CommentMapper;

import java.time.ZoneId;

@Component
public class CommentMapperImpl implements CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getImage().getId());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public Comment mapToComment(CommentDto commentDto) {
        Comment mappedComment = new Comment();
        mappedComment.setId(commentDto.getPk());
//        mappedComment.getAuthor().setId(commentDto.getAuthor());
//        mappedComment.getAuthor().getImage().setImagePath(commentDto.getAuthorImage());
//        mappedComment.getAuthor().setFirstName(commentDto.getAuthorFirstName());
//        mappedComment.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(commentDto.getCreatedAt()), ZoneId.systemDefault()));
        mappedComment.setText(commentDto.getText());
        return mappedComment;
    }
}
