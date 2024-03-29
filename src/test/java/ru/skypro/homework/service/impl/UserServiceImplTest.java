package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.mapper.UserMapper;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.service.impl.UserServiceTestConstants.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserMapper userMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    ImageService imageService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    UserServiceImpl userService;
    private User expectedUser = new User();

    @BeforeEach
    public void setUp() {
        expectedUser.setId(1);
        expectedUser.setEmail(USERNAME);
        expectedUser.setFirstName(FIRSTNAME);
        expectedUser.setLastName(LASTNAME);
        expectedUser.setPhone(PHONE);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(expectedUser));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userService.findAuthUser()).thenReturn(Optional.of(expectedUser));
    }

    @Test
    void loadUserByUsername() {
        User actual = (User) userService.loadUserByUsername(expectedUser.getEmail());
        verify(userRepository).findByEmail(USERNAME);
        assertEquals(actual.getEmail(), expectedUser.getEmail());
        assertEquals(actual.getPassword(), expectedUser.getPassword());
    }

    @Test
    void getUserDto() {
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1);
        expectedUserDto.setEmail(USERNAME);
        expectedUserDto.setFirstName(FIRSTNAME);
        expectedUserDto.setLastName(LASTNAME);
        expectedUserDto.setPhone(PHONE);

        when(userMapper.mapToUserDto(expectedUser)).thenReturn(expectedUserDto);
        UserDto actual = userService.getUserDto();
        verify(userRepository).findByEmail(USERNAME);
        verify(userMapper).mapToUserDto(expectedUser);
        assertEquals(expectedUserDto, actual);
    }

    @Test
    void updateUserDto() {
        User newUser = new User();
        UserDto newUserDto = new UserDto();
        newUser.setId(1);
        newUser.setEmail(USERNAME);
        newUser.setFirstName(NEWFIRSTNAME);
        newUser.setLastName(NEWLASTNAME);
        newUser.setPhone(NEWPHONE);
        newUserDto.setId(1);
        newUserDto.setEmail(USERNAME);
        newUserDto.setFirstName(NEWFIRSTNAME);
        newUserDto.setLastName(NEWLASTNAME);
        newUserDto.setPhone(NEWPHONE);

        when(userMapper.mapToUserDto(newUser)).thenReturn(newUserDto);
        UserDto actual = userService.updateUserDto(newUserDto);
        verify(userRepository).findByEmail(USERNAME);
        assertEquals(newUserDto, actual);
    }

    @Test
    @DisplayName("Обновление фотографии пользователя, когда она уже существует")
    void updateUserImageWhenOldImageIsNotNull() {
        Image imageOld = new Image("imageOld", new byte[20]);
        Image imageNew = new Image("imageNew", new byte[20]);
        expectedUser.setImage(imageOld);
        MockMultipartFile newImageFile = new MockMultipartFile("imageNew", new byte[30]);

        when(imageService.updateImage(newImageFile, expectedUser.getImage())).thenReturn(imageNew);
        userService.updateUserImage(newImageFile);

        verify(imageService, times(1)).updateImage(newImageFile, imageOld);
        verify(userRepository, times(1)).save(expectedUser);
        assertEquals(imageNew, expectedUser.getImage());
    }

    @Test
    @DisplayName("Обновление фотографии пользователя в случае, если её не было")
    void updateUserImageWhenOldImageIsNull() {
        Image imageNew = new Image("imageNew", new byte[20]);
        MockMultipartFile newImageFile = new MockMultipartFile("imageNew", new byte[30]);
        when(imageService.saveImage(newImageFile)).thenReturn(imageNew);

        userService.updateUserImage(newImageFile);
        verify(imageService, times(1)).saveImage(newImageFile);
        verify(userRepository, times(1)).save(expectedUser);
        assertEquals(imageNew, expectedUser.getImage());
    }
}

