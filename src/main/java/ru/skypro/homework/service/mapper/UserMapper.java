package ru.skypro.homework.service.mapper;

import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

public interface UserMapper {
    UserDto mapUserToUserDto(User user);

    User mapUserDtoToUser(UserDto userDto);

    User mapRegToUser(RegisterReqDto registerReqDto);

    User mapRegToUser (RegisterReqDto registerReqDto);
}
