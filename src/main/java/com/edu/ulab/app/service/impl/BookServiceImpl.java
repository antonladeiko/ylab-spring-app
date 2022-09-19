package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
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
public class BookServiceImpl implements BookService {

    private final BookStorage bookStorage;
    private final BookMapper bookMapper;
    private final UserStorage userStorage;
    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookStorage.save(bookMapper.bookDtoToBook(bookDto));
        userStorage.addBook(book.getUserId(), book.getId());
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookStorage.update(id, bookMapper.bookDtoToBook(bookDto));
        return bookMapper.bookToBookDto(book);
    }
    @Override
    public void deleteAllBooks(){
        bookStorage.removeAll();
    }
    @Override
    public List<BookDto> getAllBooks(){
        Collection<Book> books = bookStorage.getAll().values();
        return books.stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Book with id: " + id + " not found"));
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.remove(id);
    }
}
