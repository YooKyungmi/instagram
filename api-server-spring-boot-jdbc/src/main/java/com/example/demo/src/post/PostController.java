package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.comment.GetCommentRes;
import com.example.demo.src.post.model.postModel.GetPostRes;
import com.example.demo.src.post.model.postModel.PostPostsReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/app/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;

    public PostController(PostProvider postProvider,PostService postService,JwtService jwtService){
        this.postProvider=postProvider;
        this.postService = postService;
        this.jwtService=jwtService;
    }

    /**
     * 특정 게시물 조회 API
     * [GET] /app/posts/:post-id
     * @return
     */

    @ResponseBody
    @GetMapping("/{post-id}")
    public BaseResponse<GetPostRes> getPost(@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            GetPostRes getPostRes = postProvider.getPost(userIdByJwt,postId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사용자 게시물 조회 API
     * [GET] /app/posts/profiles/user?user-id=
     * @return
     */
    @GetMapping("/profiles/user")
    public BaseResponse<List<GetPostRes>> getPostProfile(@RequestParam("user-id") Integer searchUserId) {
        try{
            int userIdByJwt = 1;
            List<GetPostRes> getPostRes = postProvider.getPostProfile(userIdByJwt,searchUserId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사용자 팔로우 게시물 조회 API
     * [GET] /app/posts/followings
     * @return
     */
    @GetMapping("/followings")
    public BaseResponse<List<GetPostRes>> getPostFollowing() {
        try{
            int userIdByJwt = 12;
            List<GetPostRes> getPostRes = postProvider.getPostFollowing(userIdByJwt);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 댓글 조회 API
     * [GET] /app/post/comment/:post-id
     * @return
     */
    @ResponseBody
    @GetMapping("/comment/{post-id}")
    public BaseResponse<List<GetCommentRes>> getPostComments(@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetCommentRes> getPostCommentRes = postProvider.getPostComments(postId,userIdByJwt);
            return new BaseResponse<>(getPostCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 댓글에 대한 대댓글 조회 API
     * [GET] /app/post/comment/bigComment?parent-id=
     * @return
     */
    @GetMapping("/comment/bigComment")
    public BaseResponse<List<GetCommentRes>> getPostComments(@RequestParam("parent-id") Integer commentId){
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetCommentRes> getBigCommentRes = postProvider.getBigComments(commentId,userIdByJwt);
            return new BaseResponse<>(getBigCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createPost(@Valid @RequestBody PostPostsReq postPostsReq){
        try{
            int userIdByJwt = jwtService.getUserId();
             postService.createPost(postPostsReq,userIdByJwt);
             String result="";
             return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
