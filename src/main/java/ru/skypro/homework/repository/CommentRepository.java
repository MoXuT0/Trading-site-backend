package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

/**
 * Интерфейс, содержащий методы для работы с базой данных комментариев
 *
 * @see Comment
 * @see ru.skypro.homework.service.CommentService
 * @see ru.skypro.homework.service.impl.CommentServiceImpl
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
