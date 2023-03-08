package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PostProvider implements com.example.demo.src.user.spi.PostProvider {

    private final PostDao postDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostProvider(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public int getPostCount(int userId) throws BaseException {
        try {
            return postDao.getPostCount(userId);
        } catch (Exception exception) {
            logger.error("App - getPostCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
