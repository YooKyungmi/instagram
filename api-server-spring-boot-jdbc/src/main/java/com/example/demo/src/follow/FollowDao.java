package com.example.demo.src.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getFollowerCount(int userId) {
        String getFollowerCountQuery = "Select count(followerId) from UserFollow where followingId = ?";
        int getFollowerCountParams = userId;
        return this.jdbcTemplate.queryForObject(getFollowerCountQuery,
                int.class,
                getFollowerCountParams);
    }

    public int getFollowingCount(int userId) {
        String getFollowingCountQuery = "Select count(followingId) from UserFollow where followerId = ?";
        int getFollowingCountParams = userId;
        return this.jdbcTemplate.queryForObject(getFollowingCountQuery,
                int.class,
                getFollowingCountParams);
    }

    public int getConnectedFriendCount(int onlineUserId, int findingUserId) {
        String getConnectedFriendCountQuery = "Select count(followId) from UserFollow where followerId = ? AND followingId IN (Select followerId from UserFollow where followingId = ?)";
        Object[] getConnectedFriendParams = new Object[]{onlineUserId, findingUserId};
        return this.jdbcTemplate.queryForObject(getConnectedFriendCountQuery,
                int.class,
                getConnectedFriendParams);
    }

    public List<Integer> getConnectedFollowId(int onlineUserId, int findingUserId) {
        String getConnectedFollowIdQuery = "Select followingId from UserFollow where followerId = ? AND followingId IN (Select followerId from UserFollow where followingId = ?) limit 2";
        Object[] getConnectedFollowIdParams = new Object[]{onlineUserId, findingUserId};
        return this.jdbcTemplate.query(getConnectedFollowIdQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                getConnectedFollowIdParams);
    }

    public List<Integer> getConnectedFollows(int onlineUserId, int findingUserId) {
        String getConnectedFollowIdQuery = "Select followingId from UserFollow where followerId = ? AND followingId IN (Select followerId from UserFollow where followingId = ?) limit 2";
        Object[] getConnectedFollowIdParams = new Object[]{onlineUserId, findingUserId};
        return this.jdbcTemplate.query(getConnectedFollowIdQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                getConnectedFollowIdParams);
    }

    public List<Integer> getFollowers(int userId) {
        String getFollowerQuery = "Select followerId from UserFollow where followingId = ?";
        int getFollowerParams = userId;
        return this.jdbcTemplate.query(getFollowerQuery,
                (rs, rowNum) -> rs.getInt("followerId"),
                getFollowerParams);
    }

    public int checkFollowing(int onlineUserId,int userId) {
        String checkFollowingQuery = "select exists(select followId from UserFollow where followingId = ? and followerId = ?)";
        Object[] checkFollowingParams = new Object[]{onlineUserId, userId};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery,
                int.class,
                checkFollowingParams);
    }

    public List<Integer> getFollowings(int userId) {
        String getFollowingQuery = "Select followingId from UserFollow where followerId = ?";
        int getFollowingParams = userId;
        return this.jdbcTemplate.query(getFollowingQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                getFollowingParams);
    }
}
