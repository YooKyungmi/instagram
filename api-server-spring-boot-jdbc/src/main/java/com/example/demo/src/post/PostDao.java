package com.example.demo.src.post;


import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository

public class PostDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetPostRes getPost(int postId){
        String Query = "select * from Post where postId = ?";
        int postParams = postId;
        Post post= jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new Post(
                        rs.getInt("postId"),
                        rs.getInt("userId"),
                        rs.getString("content"),
                        rs.getString("place"),
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
                        rs.getString("updatedAt")),
                postParams);

        String Query2 = "SELECT COUNT(case when postId = ? then 1 end) FROM PostUser";
        int likeParams = post.getPostId();
        int likeCount = this.jdbcTemplate.queryForObject(Query2, Integer.class,likeParams);

        String Query3 = "select nickname, profileImageUrl from User where userId = ?";
        int userParams = post.getUserId();
        return this.jdbcTemplate.queryForObject(Query3,
                (rs,rowNum) -> new GetPostRes(
                        post.getPostId(),
                        post.getContent(),
                        post.getPlace(),
                        post.getCommentShowStatus(),
                        post.getPhoto1(),
                        post.getPhoto2(),
                        post.getPhoto3(),
                        post.getPhoto4(),
                        post.getPhoto5(),
                        post.getPhoto6(),
                        post.getPhoto7(),
                        post.getPhoto8(),
                        post.getPhoto9(),
                        post.getPhoto10(),
                        post.getCreatedAt(),
                        likeCount,
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")),
                userParams );

    }




    public int getPostCount(int userId) {
        String getPostCountQuery = "Select count(postId) from Post where userId = ?";
        int getPostCountParams = userId;
        return this.jdbcTemplate.queryForObject(getPostCountQuery,
                int.class,
                getPostCountParams);
    }

}
