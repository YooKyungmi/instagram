package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PostProvider {
    private final PostDao postDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService;
    }

    public GetPostRes getPost(int postId) throws BaseException {
        try {
            GetPostRes getPostRes = postDao.getPost(postId);
            return getPostRes;
        } catch (Exception exception) {
            logger.error("App - getPost Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getPostCount(int userId) throws BaseException {
        try {
            return postDao.getPostCount(userId);
        } catch (Exception exception) {
            logger.error("App - getPostCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}