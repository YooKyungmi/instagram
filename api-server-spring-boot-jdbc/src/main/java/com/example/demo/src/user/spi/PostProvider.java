package com.example.demo.src.user.spi;

import com.example.demo.config.BaseException;

public interface PostProvider {

    int getPostCount(int userId) throws BaseException;

}
