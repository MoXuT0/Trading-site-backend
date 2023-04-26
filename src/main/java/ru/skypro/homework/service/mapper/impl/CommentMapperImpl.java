package ru.skypro.homework.service.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.service.mapper.CommentMapper;

@Component
public class CommentMapperImpl implements CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getImage().getImageLink());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(Integer.parseInt(comment.getCreatedAt()));
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public Comment mapToComment(CommentDto commentDto) {
        Comment mappedComment = new Comment();
        mappedComment.setId(commentDto.getId());
        mappedComment.getAuthor().setId(commentDto.getAuthor());
        mappedComment.getAuthor().getImage().setImageLink(commentDto.getAuthorImage());
        mappedComment.getAuthor().setFirstName(commentDto.getAuthorFirstName());
        mappedComment.setCreatedAt(String.valueOf(commentDto.getCreatedAt()));
        mappedComment.setText(commentDto.getText());
        return mappedComment;
    }
}