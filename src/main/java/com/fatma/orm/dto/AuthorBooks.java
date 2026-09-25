package com.fatma.orm.dto;

import org.springframework.stereotype.Component;


public class AuthorBooks {

    private String authorName;
    private Long bookNum;

    public AuthorBooks(String authorName, Long bookNum) {
        this.authorName = authorName;
        this.bookNum = bookNum;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getBookNum() {
        return bookNum;
    }

    public void setBookNum(Long bookNum) {
        this.bookNum = bookNum;
    }

    @Override
    public String toString() {
        return "AuthorBooks{" +
                "authorName='" + authorName + '\'' +
                ", bookNum=" + bookNum +
                '}';
    }
}
