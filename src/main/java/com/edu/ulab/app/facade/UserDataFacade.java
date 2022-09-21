package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(Long userId, UserBookRequest userBookRequest) {
        log.info("Receiving a request for get user and books: {} / {}", userId, userBookRequest);
        UserDto userDto = userService.getUserById(userId);
        log.info("User id received:: {} / {}", userId, userBookRequest);

        userDto = userService
                .updateUser(userId, userMapper.userRequestToUserDto(userBookRequest.getUserRequest()));

        List<Long> booksIdList = userService.booksIdList(userDto);
        booksIdList.forEach(bookService::deleteBookById);

        booksIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userId))
                .map(bookService::createBook)
                .map(BookDto::getId)
                .toList();

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Receiving a request for get user by id: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("The user received: {}", userDto);

        log.info("Getting a list of book id by user id: {}", userDto.getId());
        List<Long> booksIdList = userService.booksIdList(userDto);
        log.info("List id received: {}", booksIdList);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        UserDto userDto = userService.getUserById(userId);

        log.info("Getting a list of book id by user id: {}", userDto.getId());
        List<Long> booksIdList = userService.booksIdList(userDto);
        booksIdList.forEach(bookService::deleteBookById);
        log.info("List was removed");

        log.info("Receiving a request for delete user by id: {}", userId);
        userService.deleteUserById(userId);
        log.info("The user was removed");
    }

}
