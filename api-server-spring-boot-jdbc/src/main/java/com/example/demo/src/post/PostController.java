package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.comment.GetCommentRes;
import com.example.demo.src.post.model.comment.PostCommentReq;
import com.example.demo.src.post.model.comment.PostCommentRes;
import com.example.demo.src.post.model.postModel.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return BaseResponse<GetPostRes>
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
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/profiles/user")
    public BaseResponse<List<GetPostRes>> getPostProfile(@RequestParam("user-id") Integer searchUserId) {
        try{

            int userIdByJwt = jwtService.getUserId();
            List<GetPostRes> getPostRes = postProvider.getPostProfile(userIdByJwt,searchUserId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스크랩 게시물 전체 조회 API
     * [GET] /app/posts/scraped
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/scraped")
    public BaseResponse<List<GetPostRes>> getPostScrap() {
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetPostRes> getPostRes = postProvider.getPostScrap(userIdByJwt);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사용자 팔로우 게시물 조회 API
     * [GET] /app/posts/followings
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/followings")
    public BaseResponse<List<GetPostRes>> getPostFollowing() {
        try{

            int userIdByJwt = jwtService.getUserId();
            List<GetPostRes> getPostRes = postProvider.getPostFollowing(userIdByJwt);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * contentTag로 게시물 검색 API (게시물목록)
     * [GET] /app/posts/content-tag?tag-word=
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/content-tag")
    public BaseResponse<List<GetPostRes>> getPostContentTag(@RequestParam("tag-word") String tagWord) {
        try{

            int userIdByJwt = jwtService.getUserId();
            List<GetPostRes> getPostRes = postProvider.getPostContentTag(userIdByJwt,tagWord);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * userTag로 게시물 검색 API (게시물목록)
     * [GET] /app/posts/user-tag?user-id=
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/user-tag")
    public BaseResponse<List<GetPostRes>> getPostUserTag(@RequestParam("user-id")Integer userTagId) {
        try{

            int userIdByJwt = jwtService.getUserId();
            List<GetPostRes> getPostRes = postProvider.getPostUserTag(userIdByJwt,userTagId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 게시물 조회 API
     * [GET] /app/posts/:post-id
     * @return BaseResponse<GetPostRes>
     */
    @ResponseBody
    @GetMapping("/recommended")
    public BaseResponse<List<GetPostRecommendRes>> getPostRecommend(){
        try{
            List<GetPostRecommendRes> getPostRecommendRes = postProvider.getPostRecommend();
            return new BaseResponse<>(getPostRecommendRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 댓글 조회 API
     * [GET] /app/posts/comments/:post-id
     * @return  BaseResponse<List<GetCommentRes>>
     */
    @ResponseBody
    @GetMapping("/comments/{post-id}")
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
     * [GET] /app/posts/comments/bigComment?parent-id=
     * @return BaseResponse<List<GetCommentRes>>
     */
    @GetMapping("/comments/big-comment")
    public BaseResponse<List<GetCommentRes>> getPostComments(@RequestParam("parent-id") Integer commentId){
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetCommentRes> getBigCommentRes = postProvider.getBigComments(commentId,userIdByJwt);
            return new BaseResponse<>(getBigCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 작성 API
     * [POST] /app/posts
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPost(@Valid @RequestBody PostPostsReq postPostsReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            PostPostsRes postPostsRes = postService.createPost(postPostsReq,userIdByJwt);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 좋아요 추가 API
     * [POST] /app/posts/likes/:post-id
     * @return BaseResponse<String>
     */


    @PostMapping("/likes/{post-id}")
    public BaseResponse<String> addPostLike(@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.addPostLike(postId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 게시물 스크랩 추가 API
     * [POST] /app/posts/srcaped/:post-id
     * @return BaseResponse<String>
     */

    @PostMapping("/scraped/{post-id}")
    public BaseResponse<String> addPostScrap(@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.addPostScrap(postId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 태그 추가 API
     * [POST] /app/posts/content-tag
     * @return BaseResponse<String>
     */

    @PostMapping("/content-tag")
    public BaseResponse<String> addContentTag (@Valid @RequestBody PostContentTagReq postContentTagReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.addContentTag(postContentTagReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 사용자 태그 추가 API
     * [POST] /app/posts/user-tag
     * @return BaseResponse<String>
     */

    @PostMapping("/user-tag")
    public BaseResponse<String> addUserTag (@Valid @RequestBody PostUserTagReq postUserTagReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.addUserTag(postUserTagReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 게시물 사용자 태그 삭제 API
     * [PATCH] /app/posts/user-tag/deleted
     * @return BaseResponse<String>
     */

    @PatchMapping("/user-tag/deleted")
    public BaseResponse<String> deleteUserTag (@Valid @RequestBody PatchUserTagReq patchUserTagReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deleteUserTag(patchUserTagReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 사진 삭제 API
     * [PATCH] /app/posts/picture/deleted
     * @return BaseResponse<String>
     */

    @PatchMapping("/picture/deleted")
    public BaseResponse<String> deletePhoto (@Valid @RequestBody DeletePhotoReq deletePhotoReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deletePhoto(deletePhotoReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 태그 삭제 API
     * [PATCH] /app/posts/content-tag/deleted
     * @return BaseResponse<String>
     */

    @PatchMapping("/content-tag/deleted")
    public BaseResponse<String> deleteContentTag (@Valid @RequestBody PatchObjectReq patchObjectReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deleteContentTag(patchObjectReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 장소 수정 API
     * [PATCH] /app/posts/place
     * @return BaseResponse<String>
     */

    @PatchMapping("/place")
    public BaseResponse<String> updatePlace (@Valid @RequestBody PatchObjectReq patchObjectReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updatePlace(patchObjectReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 게시물 장소 삭제 API
     * [PATCH] /app/posts/place/deleted/:post-id
     * @return BaseResponse<String>
     */

    @PatchMapping("/place/deleted/{post-id}")
    public BaseResponse<String> deletePlace (@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deletePlace(postId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 설명 수정 API
     * [PATCH] /app/posts/content
     * @return BaseResponse<String>
     */

    @PatchMapping("/content")
    public BaseResponse<String> updatePostsContent (@Valid @RequestBody PatchObjectReq patchObjectReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updatePostsContent(patchObjectReq,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 좋아요 여부 수정 API
     * [PATCH] /app/posts/showed-like/{post-id}/{status}
     * @return BaseResponse<String>
     */
    @PatchMapping("/showed-like/{post-id}/{status}")
    public BaseResponse<String> updateLikeShowStatus(@PathVariable("post-id") int postId,
                                                     @PathVariable("status") boolean status){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updateLikeShowStatus(postId,status,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 댓글 기능 여부 수정 API
     * [PATCH] /app/posts/showed-comment/:post-id/:status
     * @return BaseResponse<String>
     */
    @PatchMapping("/showed-comment/{post-id}/{status}")
    public BaseResponse<String> updateCommentShowStatus(@PathVariable("post-id") int postId,
                                                        @PathVariable("status") boolean status){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updateCommentShowStatus(postId,status,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 좋아요 여부 수정 API
     * [PATCH] /app/posts/likes/:like-id/:status
     * @return BaseResponse<String>
     */
    @PatchMapping("/likes/{like-id}/{status}")
    public BaseResponse<String> updatePostLikeOn(@PathVariable("like-id") int postLikeId,
                                                 @PathVariable("status") boolean status){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updatePostLikeOn(postLikeId,status,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 스크랩 여부 수정 API
     * [PATCH] /app/posts/scraped/:scrap-id/:status
     * @return BaseResponse<String>
     */
    @PatchMapping("/scraped/{scrap-id}/{status}")
    public BaseResponse<String> updateScrapOn(@PathVariable("scrap-id") int scrapId,
                                              @PathVariable("status") boolean status){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updateScrapOn(scrapId,status,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 좋아요 여부 수정 API
     * [PATCH] /app/posts/comments/like-status/:like-id/:status
     * @return BaseResponse<String>
     */
    @PatchMapping("/comments/like-status/{like-id}/{status}")
    public BaseResponse<String> updateCommentLikeOn(@PathVariable("like-id") int commentLikeId,
                                                    @PathVariable("status") boolean status){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.updateCommentLikeOn(commentLikeId,status,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 댓글 좋아요 추가 API
     * [POST] /app/posts/comments/like-status/:comment-id
     * @return BaseResponse<String>
     */

    @PostMapping("/comments/like-status/{comment-id}")
    public BaseResponse<String> addCommentLike(@PathVariable("comment-id") int commentId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.addCommentLike(commentId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 작성 API
     * [POST] /app/posts/comments
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/comments")
    public BaseResponse<PostCommentRes> createComment(@Valid @RequestBody PostCommentReq postCommentReq){
        try{
            int userIdByJwt = jwtService.getUserId();
            PostCommentRes postCommentRes = postService.createComment(postCommentReq,userIdByJwt);
            return new BaseResponse<>(postCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물 삭제 API
     * [PATCH] /app/posts/deleted/:post-id
     * @return BaseResponse<String>
     */
    @PatchMapping("/deleted/{post-id}")
    public BaseResponse<String> deletePost(@PathVariable("post-id") int postId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deletePost(postId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 삭제 API
     * [PATCH] /app/posts/comments/deleted/:comment-id
     * @return BaseResponse<String>
     */
    @PatchMapping("/comments/deleted/{comment-id}")
    public BaseResponse<String> deleteComment(@PathVariable("comment-id") int commentId){
        try{
            int userIdByJwt = jwtService.getUserId();
            postService.deleteComment(commentId,userIdByJwt);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}