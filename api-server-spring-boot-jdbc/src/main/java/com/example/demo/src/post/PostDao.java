package com.example.demo.src.post;


import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.post.model.comment.Comment;
import com.example.demo.src.post.model.comment.GetCommentRes;
import com.example.demo.src.post.model.comment.PostCommentReq;
import com.example.demo.src.post.model.postModel.Photo;
import com.example.demo.src.post.model.postModel.GetPostRes;
import com.example.demo.src.post.model.postModel.Post;
import com.example.demo.src.post.model.postModel.PostPostsReq;
import com.example.demo.src.story.StoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository

public class PostDao {
    private JdbcTemplate jdbcTemplate;
    private final StoryDao storyDao;
    private final FollowDao followDao;

    public PostDao(StoryDao storyDao, FollowDao followDao) {
        this.storyDao = storyDao;
        this.followDao = followDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetPostRes getPost(int postId, int userId){
        Post post= getPostModel(postId);
        int likeCount = getPostLikeCount(postId);
        List<Photo> photoTagList = new ArrayList<>();
        List<String> photoList = new ArrayList<>(Arrays.asList(post.getPhoto1(),post.getPhoto2(),post.getPhoto3(),
                post.getPhoto4(),post.getPhoto5(),post.getPhoto6(),
                post.getPhoto7(),post.getPhoto8(),post.getPhoto9(),post.getPhoto10()));

        photoList.removeAll(Arrays.asList("", null));
        for (String photo : photoList ){
            photoTagList.add(new Photo(photo, getPostPhotos(photo,postId)));
        }
        String Query = "select nickname, profileImageUrl from User where userId = ? and status = true";
        int userParams = post.getUserId();
        return this.jdbcTemplate.queryForObject(Query,
                (rs,rowNum) -> new GetPostRes(
                        post.getPostId(),
                        post.getUserId(),
                        post.getContent(),
                        post.getPlace(),
                        post.getLikeShowStatus(),
                        post.getCommentShowStatus(),
                        photoTagList,
                        post.getCreatedAt(),
                        post.getUpdatedAt(),
                        likeCount,
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl"),
                        getScrapOn(postId,userId),
                        getTagOn(postId),
                        getPostLikeOn(postId,userId),
                        storyDao.checkStory(post.getUserId()),
                        followDao.checkFollowing(userId,post.getUserId())),
                userParams );
    }
    public Post getPostModel(int postId){
        String Query = "select * from Post where postId = ?";
        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new Post(
                        rs.getInt("postId"),
                        rs.getInt("userId"),
                        rs.getString("content"),
                        rs.getString("place"),
                        rs.getInt("likeShowStatus"),
                        rs.getInt("commentShowStatus"),
                        rs.getString("photoUrl1"),
                        rs.getString("photoUrl2"),
                        rs.getString("photoUrl3"),
                        rs.getString("photoUrl4"),
                        rs.getString("photoUrl5"),
                        rs.getString("photoUrl6"),
                        rs.getString("photoUrl7"),
                        rs.getString("photoUrl8"),
                        rs.getString("photoUrl9"),
                        rs.getString("photoUrl10"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        rs.getInt("status")),
                postId);
    }
    public int getPostLikeCount(int postId){
        String Query = "SELECT COUNT(case when postId = ? and postLikeStatus = true then 1 end) FROM PostUser";
        int likeParams = postId;
        return this.jdbcTemplate.queryForObject(Query, Integer.class,likeParams);
    }

    public int getCommentLikeCount(int commentId){
        String Query = "SELECT COUNT(case when commentId = ? and status = true then 1 end) FROM CommentLike";
        int likeParams = commentId;
        return this.jdbcTemplate.queryForObject(Query, Integer.class,likeParams);
    }

    public int getBigCommentCount(int commentId){
        String Query = "SELECT COUNT(case when groupId = ? and status = true then 1 end) FROM Comment";
        int params = commentId;
        return this.jdbcTemplate.queryForObject(Query, Integer.class,params);
    }

    public List<String> getPostPhotos(String photoUrl,int postId){
        String Query ="select userId from UserTag where photoUrl = ? and postId = ? and status = true";
        Object[] params = new Object[]{photoUrl,postId};
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getString("userId") ,params);
    }


    public int getScrapOn(int postId,int userId){
        String Query = "select status from Scrap where userId = ? and postId = ? ";
        Object[] params = new Object[]{userId,postId};
        try{
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                params);
        }catch (EmptyResultDataAccessException e) {
            return 0;

        }
    }

    public List<String> getTagOn(int postId){
        String Query = "select tagWord from ContentTag where status = true and postId = ?";
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getString("tagWord") ,postId);
    }

    public int getPostLikeOn(int postId, int userId){
        String Query = "select postLikeStatus from PostUser where userId = ? and postId = ?";
        Object[] params = new Object[]{userId,postId};
        try{
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                params);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public int getCommentLikeOn(int commentId, int userId){
        String Query = "select status from CommentLike where userId = ? and commentId = ?";
        Object[] params = new Object[]{userId,commentId};
        try{
            return this.jdbcTemplate.queryForObject(Query,
                    int.class,
                    params);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public List<GetPostRes> getPostProfile(int userId, int searchUserId){
        List<GetPostRes> getPostResList  = new ArrayList<>();
        String Query ="select postId from Post where userId = ? and status = true order by createdAt DESC";
        List<Integer> postList = this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getInt("postId") ,searchUserId);
        for (int postId : postList ){
            getPostResList.add(getPost(postId, userId));
        }
        return getPostResList;
    }

    public List<GetPostRes> getPostFollowing(int userId,List<Integer> followingsList){
        List<GetPostRes> getPostResList  = new ArrayList<>();
        String followList=followingsList.toString();
        String Query ="select postId from Post where userId in ("+followList.substring(1, followList.length()-1) +") and status = true order by createdAt DESC";
        List<Integer> postList = this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getInt("postId"));
        for (int postId : postList ){
            getPostResList.add(getPost(postId, userId));
        }
        return getPostResList;
    }

    public Comment getCommentModel(int commentId){
        String Query = "select * from Comment where commentId = ?";
        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new Comment(
                        rs.getInt("commentId"),
                        rs.getInt("postId"),
                        rs.getInt("userId"),
                        rs.getInt("groupId"),
                        rs.getString("comment"),
                        rs.getInt("status") ,
                        rs.getString("updatedAt"),
                        rs.getString("createdAt")),
                commentId);
    }

    public GetCommentRes getComment(int commentId, int userId){
        Comment comment = getCommentModel(commentId);
        String Query = "select nickname, profileImageUrl from User where userId = ? and status = true";
        int userParam = comment.getUserId();
        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new GetCommentRes(
                        comment.getCommentId(),
                        comment.getPostId(),
                        comment.getUserId(),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl"),
                        comment.getGroupId(),
                        comment.getComment(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        getBigCommentCount(commentId),
                        getCommentLikeCount(commentId),
                        getCommentLikeOn(commentId,userId)),
                userParam);
    }

    public List<GetCommentRes> getPostComments(int postId, int userId){
        String Query = "Select commentId from Comment where postId = ? and groupId is null and status = true";
        List<GetCommentRes> postCommentList = new ArrayList<>();
        List<Integer> commentIdList = this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getInt("commentId"),
                postId);
        for (int commentId : commentIdList ){
            postCommentList.add(getComment(commentId,userId));
        }

        return postCommentList;
    }

    public List<GetCommentRes> getBigComments(int parentCommentId, int userId){
        String Query = "Select commentId from Comment where groupId = ? and status = true";
        List<GetCommentRes> postBigCommentList = new ArrayList<>();
        List<Integer> commentIdList = this.jdbcTemplate.query(Query,
                (rs, rowNum) -> rs.getInt("commentId"),
                parentCommentId);
        for (int commentId : commentIdList ){
            postBigCommentList.add(getComment(commentId,userId));
        }
        return postBigCommentList;
    }


    public int getPostCount(int userId) {
        String getPostCountQuery = "Select count(postId) from Post where userId = ?";
        int getPostCountParams = userId;
        return this.jdbcTemplate.queryForObject(getPostCountQuery,
                int.class,
                getPostCountParams);
    }


    public int createPost(PostPostsReq postPostsReq, int userId){
        List<String> photoList = new ArrayList<>();
        for (Photo photo : postPostsReq.getPhotos() ){
            photoList.add(photo.getPhotoUrl());
        }
        String photoUrlList=String.join("\",\"",photoList);
        photoUrlList="\""+photoUrlList+"\"";
        photoUrlList = photoUrlList + ",null".repeat(10 - photoList.size());
        String insertPostQuery = "insert into Post values(null,?,?,?,?,?,"+photoUrlList+",1,current_timestamp,current_timestamp)";
        Object[] postParams = new Object[]{userId,
                postPostsReq.getContent(),
                postPostsReq.getPlace(),
                postPostsReq.getLikeShowStatus(),
                postPostsReq.getCommentShowStatus()
        };
        this.jdbcTemplate.update(insertPostQuery, postParams);
        String lastInsertIdQuery = "select last_insert_id()";
        int postId =  this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

        String UserTagQuery = "insert into UserTag (userId, postId, photoUrl) values (?,?,?)";
        for (Photo photo : postPostsReq.getPhotos() ) {
            for (String userTagId : photo.getUserTagId()) {
                Object[] userTagParams = new Object[]{userTagId, postId, photo.getPhotoUrl()};
                this.jdbcTemplate.update(UserTagQuery, userTagParams);
            }
        }
        String ContentTagQuery = "insert into UserTag (userId, postId, photoUrl) values (?,?,?)";
        if (postPostsReq.getTagWord().size()>0) {
            for (String tagWord : postPostsReq.getTagWord()) {
                Object[] ContentTagParams = new Object[]{postId, tagWord};
                this.jdbcTemplate.update(ContentTagQuery, ContentTagParams);
            }
        }
        return postId;
    }

    public void addPostLike (int postId,int userId){
        String Query = "insert into PostUser (userId,postId) values (?,?)";
        Object[] params = new Object[]{userId, postId};
        this.jdbcTemplate.update(Query,params);
    }

    public void addPostScrap (int postId,int userId){
        String Query = "insert into Scrap (userId,postId) values (?,?)";
        Object[] params = new Object[]{userId, postId};
        this.jdbcTemplate.update(Query,params);
    }

    public void addCommentLike (int commentId,int userId){
        String Query = "insert into CommentLike (userId,commentId) values (?,?)";
        Object[] params = new Object[]{userId, commentId};
        this.jdbcTemplate.update(Query,params);
    }

    public int createComment (int userId, PostCommentReq postCommentReq) {
        String Query = "insert into Comment (userId,postId,groupId,comment) values (?,?,?,?)";
        Object[] params = new Object[]{userId, postCommentReq.getPostId(), postCommentReq.getGroupId(), postCommentReq.getComment()};
        this.jdbcTemplate.update(Query, params);
        String lastInsertIdQuery = "select last_insert_id()";
        int commentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
        String mentionQuery = "insert into mention (userId, commentId) values (?,?)";
        if (postCommentReq.getMention().size()>0){
            for (Integer mentionId : postCommentReq.getMention()) {
                Object[] mentionParams = new Object[]{mentionId, commentId};
                this.jdbcTemplate.update(mentionQuery, mentionParams);
            }
        }
        return commentId;
    }
}
