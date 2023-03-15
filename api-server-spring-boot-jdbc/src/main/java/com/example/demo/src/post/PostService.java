package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.comment.PostCommentReq;
import com.example.demo.src.post.model.comment.PostCommentRes;
import com.example.demo.src.post.model.postModel.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public PostPostsRes createPost(PostPostsReq postPostsReq, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            PostPostsRes postPostsRes = PostPostsRes.builder()
                    .postId(postDao.createPost(postPostsReq, userId))
                    .build();

            return postPostsRes;
        } catch (Exception exception) {
            logger.error("Post - createPost Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void addPostLike(int postId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            postDao.addPostLike(postId, userId);
        } catch (Exception exception) {
            logger.error("Post - addPostLike Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void addPostScrap(int postId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            postDao.addPostScrap(postId, userId);
        } catch (Exception exception) {
            logger.error("Post - addPostScrap Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void addCommentLike (int commentId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            postDao.addCommentLike(commentId, userId);
        } catch (Exception exception) {
            logger.error("Post - addPostScrap Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void addContentTag (PostContentTagReq postContentTagReq, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.addContentTag(postContentTagReq.getPostId(), postContentTagReq.getTagWord());
        }catch (Exception exception) {
            logger.error("Post - addContentTag Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void addUserTag(PostUserTagReq postUserTagReq, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.addUserTag(postUserTagReq.getPostId(), postUserTagReq.getPhotos());
        }catch (Exception exception) {
            logger.error("Post - addUserTag Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }
    @Transactional
    public void updatePlace (PatchObjectReq patchObjectReq, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updatePlace(patchObjectReq.getPostId(), patchObjectReq.getDetail(),userId);
        }catch (Exception exception) {
            logger.error("Post - updatePlace Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }
    @Transactional
    public void updatePostsContent (PatchObjectReq patchObjectReq, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            postDao.updatePostsContent(patchObjectReq.getPostId(), patchObjectReq.getDetail(),userId);
        } catch (Exception exception) {
            logger.error("Post - updatePostsContent Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }

    @Transactional
    public void updateLikeShowStatus(int postId, boolean status, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updateLikeShowStatus(postId,status,userId);
        }catch (Exception exception) {
        logger.error("Post - updateLikeShowStatus Service Error", exception);
        throw new BaseException(PATCH_FAILED);
        }
    }
    @Transactional
    public void updateCommentShowStatus(int postId, boolean status, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updateCommentShowStatus(postId,status,userId);
        }catch (Exception exception) {
            logger.error("Post - updateCommentShowStatus Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }
    @Transactional
    public void updatePostLikeOn(int postLikeId, boolean status, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updatePostLikeOn(postLikeId,status,userId);
        }catch (Exception exception) {
            logger.error("Post - updatePostLikeOn Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }
    @Transactional
    public void updateScrapOn(int scrapId, boolean status, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updateScrapOn(scrapId,status,userId);
        }catch (Exception exception) {
            logger.error("Post - updateScrapOn Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }

    @Transactional
    public void updateCommentLikeOn(int commentLikeId, boolean status, int userId) throws BaseException{
        throwIfInvalidUserIdDetected(userId);
        try{
            postDao.updateCommentLikeOn(commentLikeId,status,userId);
        }catch (Exception exception) {
            logger.error("Post - updateCommentLikeOn Service Error", exception);
            throw new BaseException(PATCH_FAILED);
        }
    }

    @Transactional
    public PostCommentRes createComment(PostCommentReq postCommentReq, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            PostCommentRes postCommentRes = PostCommentRes.builder()
                    .commentId(postDao.createComment(userId,postCommentReq))
                    .build();
            return postCommentRes;
        } catch (Exception exception) {
            logger.error("Post - createComment Service Error", exception);
            throw new BaseException(POST_FAILED);
        }
    }



    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }
}

