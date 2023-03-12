package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.postModel.PostPostsReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.POST_FAILED;

@Service

public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
    }

    public void createPost(PostPostsReq postPostsReq, int userId) throws BaseException{
        try{
        postDao.createPost(postPostsReq,userId);
        } catch (Exception exception) {
            logger.error("Post - createPost Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
}
