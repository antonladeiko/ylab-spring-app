package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userStorage.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userStorage.update(id, userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }
    @Override
    public List<UserDto> getAllUsers(){
        Collection<User> users = userStorage.getAll().values();
        return users.stream()
                .filter(Objects::nonNull)
                .map(userMapper::userToUserDto)
                .toList();
    }
    @Override
    public void deleteAllUsers(){
        userStorage.removeAll();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.remove(id);
    }

    public List<Long> booksIdList(UserDto userDto){
        User user = userStorage.get(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Fuck!"));
        return user.getBookList();
    }


}
