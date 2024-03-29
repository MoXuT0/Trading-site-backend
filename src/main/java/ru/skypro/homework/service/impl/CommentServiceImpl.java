package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserForbiddenException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.mapper.CommentMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс - сервис, содержащий реализацию интерфейса {@link CommentService}
 *
 * @see Comment
 * @see CommentRepository
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;
    private final UserService userService;

    /**
     * Метод ищет и возвращает список всех комментариев {@link ResponseWrapperCommentDto} к объявлению по id объявления
     *
     * @param adId
     * @return {@link CommentRepository#findAll()}, {@link CommentMapper#mapToCommentDto(Comment)},
     * @see CommentMapper
     */
    @Override
    public ResponseWrapperCommentDto getCommentsDto(Integer adId) {
        List<Comment> commentList = commentRepository.findAll();
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            if (adId.equals(comment.getAd().getId())) {
                commentDtoList.add(commentMapper.mapToCommentDto(comment));
            }
        }
        return new ResponseWrapperCommentDto(commentDtoList);
    }

    /**
     * Метод создает комментарий к объявлению по id объявления
     *
     * @param adId
     * @param commentDto
     * @return {@link CommentRepository#save(Object)}, {@link CommentMapper#mapToCommentDto(Comment)}
     * @throws AdsNotFoundException  если объявление по указанному id не найдено
     * @throws UserNotFoundException если пользователь не найден
     * @see CommentMapper
     */
    @Override
    public CommentDto createCommentDto(Integer adId, CommentDto commentDto) {
        Ad ad = adRepository.findById(adId).orElseThrow(AdsNotFoundException::new);
        Comment comment = commentMapper.mapToComment(commentDto);
        comment.setAuthor(userService.findAuthUser().orElseThrow(UserNotFoundException::new));
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    /**
     * Метод удаляет комментарий к объявлению по id объявления
     *
     * @param adId
     * @param commentId
     * @return {@link CommentRepository#delete(Object)}
     * @throws UserForbiddenException если нет прав на удаление комментария
     */
    @Override
    public boolean removeCommentDto(Integer adId, Integer commentId) {
        if (checkAccess(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        throw new UserForbiddenException();
    }

    /**
     * Метод редактирует комментарий к объявлению по id
     *
     * @param adId
     * @param commentId
     * @param commentDto
     * @return {@link CommentRepository#save(Object)}, {@link CommentMapper#mapToCommentDto(Comment)}
     * @throws CommentNotFoundException если комментарий не найден
     * @throws UserForbiddenException   если нет прав на обновление комментария
     * @see CommentMapper
     */
    @Override
    public CommentDto updateCommentDto(Integer adId, Integer commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (checkAccess(commentId)) {
            comment.setText(commentDto.getText());
            commentRepository.save(comment);
            return commentMapper.mapToCommentDto(comment);
        }
        throw new UserForbiddenException();
    }

    /**
     * Метод проверяет наличие доступа к комментарию по id
     *
     * @param id
     * @throws CommentNotFoundException если комментарий не найден
     * @see UserService
     */
    @Override
    public boolean checkAccess(Integer id) {
        Role role = Role.ADMIN;
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        Optional<User> user = userService.findAuthUser();
        User notOptionalUser = user.get();
        String currentPrincipalName = notOptionalUser.getUsername();
        return comment.getAuthor().getUsername().equals(currentPrincipalName)
                || notOptionalUser.getAuthorities().contains(role);
    }
}
