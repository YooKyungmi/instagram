package com.example.demo.src.follow;

import com.example.demo.src.follow.model.GetConnectedFollowRes;
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
}
