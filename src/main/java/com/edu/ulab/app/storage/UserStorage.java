package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserStorage extends Storage<User>{
    public void addBook(Long userId, Long bookId) {
        User user = get(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
        user.addBook(bookId);
    }
}
