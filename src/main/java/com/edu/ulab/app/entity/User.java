package com.edu.ulab.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class User extends Essence{

    private String fullName;

    private String title;

    private int age;

    private List<Long> bookList;

    public void addBook(Long id){
        bookList.add(id);
    }
}
