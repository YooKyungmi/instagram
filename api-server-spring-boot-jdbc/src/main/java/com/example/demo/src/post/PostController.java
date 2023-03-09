package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            GetPostRes getPostRes = postProvider.getPost(postId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/user")
    public BaseResponse<List<GetPostRes>> getPostProfile(@RequestParam("user-id") Integer searchUserId) {
        try{
            List<GetPostRes> getPostRes = postProvider.getPostProfile(searchUserId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
