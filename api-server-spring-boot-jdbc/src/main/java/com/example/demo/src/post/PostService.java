package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.postModel.PostPostsReq;
import com.example.demo.src.post.model.postModel.PostPostsRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, UserProvider userProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public PostPostsRes createPost(PostPostsReq postPostsReq, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
        PostPostsRes postPostsRes = PostPostsRes.builder()
                .postId(postDao.createPost(postPostsReq,userId))
                .build();

        return postPostsRes;
        } catch (Exception exception) {
            logger.error("Post - createPost Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }


    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }
}
