package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.post.model.comment.GetCommentRes;
import com.example.demo.src.post.model.postModel.GetPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PostProvider {
    private final PostDao postDao;
    private final JwtService jwtService;
    private final FollowDao followDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService,FollowDao followDao) {
        this.postDao = postDao;
        this.jwtService = jwtService;
        this.followDao = followDao;
    }

    public GetPostRes getPost(int userIdByJwt,int postId) throws BaseException {
        try {
            GetPostRes getPostRes = postDao.getPost(postId,userIdByJwt); //수정 필요
            return getPostRes;

        } catch (Exception exception) {

            logger.error("Post - getPost Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostRes> getPostProfile(int userIdByJwt, int searchUserId) throws BaseException {
        try {
            List<GetPostRes> getPostRes = postDao.getPostProfile(userIdByJwt,searchUserId); //수정 필요
            return getPostRes;

        } catch (Exception exception) {
            logger.error("Post - getPostProfile Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostRes> getPostFollowing(int userIdByJwt) throws BaseException {
        try {
            List<Integer> followingsList = followDao.getFollowings(userIdByJwt);
            List<GetPostRes> getPostRes = postDao.getPostFollowing(userIdByJwt,followingsList); //수정 필요
            return getPostRes;
        } catch (Exception exception) {
            logger.error("Post - getPostFollowing Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCommentRes> getPostComments(int postId, int userId) throws BaseException {
        try {
            List<GetCommentRes> getPostCommentsList = postDao.getPostComments(postId, userId);
            return getPostCommentsList;

        } catch (Exception exception) {
            logger.error("Post - getPostComments Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCommentRes> getBigComments(int commentId, int userId) throws BaseException {
        try {
            List<GetCommentRes> getBigCommentsList = postDao.getBigComments(commentId, userId);
            return getBigCommentsList;

        } catch (Exception exception) {
            logger.error("Post - getBigComments Provider Error", exception);
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
