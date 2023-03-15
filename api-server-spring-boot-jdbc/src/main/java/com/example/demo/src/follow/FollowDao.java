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
        String getFollowerCountQuery = "Select count(followerId) from UserFollow where followingId = ? and status = 1 and followerId IN (select userId from User where accountStatus = 'ACTIVE')";
        int getFollowerCountParams = userId;
        return this.jdbcTemplate.queryForObject(getFollowerCountQuery,
                int.class,
                getFollowerCountParams);
    }

    public int getFollowingCount(int userId) {
        String getFollowingCountQuery = "Select count(followingId) from UserFollow where followerId = ? and status = 1 and followingId IN (select userId from User where accountStatus = 'ACTIVE')";
        int getFollowingCountParams = userId;
        return this.jdbcTemplate.queryForObject(getFollowingCountQuery,
                int.class,
                getFollowingCountParams);
    }

    public int getConnectedFriendCount(int onlineUserId, int findingUserId) {
        String getConnectedFriendCountQuery = "Select count(followId) from UserFollow where followerId = ? AND status = 1 AND followingId IN (Select followerId from UserFollow where followingId = ? and status = 1 and followerId IN (select userId from User where accountStatus = 'ACTIVE' and userId != ?))";
        Object[] getConnectedFriendParams = new Object[]{onlineUserId, findingUserId, onlineUserId};
        return this.jdbcTemplate.queryForObject(getConnectedFriendCountQuery,
                int.class,
                getConnectedFriendParams);
    }

    public List<Integer> getConnectedFollows(int onlineUserId, int findingUserId) {
        String getConnectedFollowIdQuery = "Select followingId from UserFollow where followerId = ? AND status = 1 AND followingId IN (Select followerId from UserFollow where followingId = ? and status = 1 and followerId IN (select userId from User where accountStatus = 'ACTIVE' and userId != ?)) limit 2";
        Object[] getConnectedFollowIdParams = new Object[]{onlineUserId, findingUserId, onlineUserId};
        return this.jdbcTemplate.query(getConnectedFollowIdQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                getConnectedFollowIdParams);
    }

    public List<Integer> getFollowers(int userId) {
        String getFollowerQuery = "Select followerId from UserFollow where followingId = ? and status = 1 and followerId IN (select userId from User where accountStatus = 'ACTIVE')";
        int getFollowerParams = userId;
        return this.jdbcTemplate.query(getFollowerQuery,
                (rs, rowNum) -> rs.getInt("followerId"),
                getFollowerParams);
    }

    public int checkFollowing(int onlineUserId,int userId) {
        String checkFollowingQuery = "select exists(select followId from UserFollow where followingId = ? and followerId = ? and status = 1)";
        Object[] checkFollowingParams = new Object[]{userId, onlineUserId};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery,
                int.class,
                checkFollowingParams);
    }

    public List<Integer> getFollowings(int userId) {
        String getFollowingQuery = "Select followingId from UserFollow where followerId = ? and status = 1 and followingId IN (select userId from User where accountStatus = 'ACTIVE')";
        int getFollowingParams = userId;
        return this.jdbcTemplate.query(getFollowingQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                getFollowingParams);
    }

    public int checkUserFollow(int onlineUserId,int userId) {
        String checkFollowingQuery = "select exists(select followId from UserFollow where followingId = ? and followerId = ?)";
        Object[] checkFollowingParams = new Object[]{userId, onlineUserId};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery,
                int.class,
                checkFollowingParams);
    }

    public int getUserFollowId(int userId, int followUserId) {
        String checkFollowingQuery = "select followId from UserFollow where followerId = ? and followingId = ?";
        Object[] checkFollowingParams = new Object[]{userId, followUserId};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery,
                int.class,
                checkFollowingParams);
    }

    public int addFollows(int userId, int followUserId) {
        String createUserQuery = "insert into UserFollow (followerId, followingId) VALUES (?,?)";
        Object[] createUserParams = new Object[]{userId,followUserId};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int patchFollows(int status, int userId, int followUserId) {
        String modifyUserFollowStatusQuery = "update UserFollow set status = ? where followerId = ? and followingId = ? ";
        Object[] modifyUserFollowStatusParams = new Object[]{status, userId, followUserId};

        return this.jdbcTemplate.update(modifyUserFollowStatusQuery,modifyUserFollowStatusParams);
    }

    public List<Integer> getFollowId(int onlineUserId) {
        String getFollowingQuery = "select followingId from UserFollow where followerId = ? and status = 1 and followingId IN (select userId from User where accountStatus = 'ACTIVE')";
        Object[] checkFollowingParams = new Object[]{onlineUserId};
        return this.jdbcTemplate.query(getFollowingQuery,
                (rs, rowNum) -> rs.getInt("followingId"),
                checkFollowingParams);
    }

    public String getConnectedFriedNickname(int userIdByJwt, int userId) {
        String getConnectedFriendNicknameQuery = "Select u.nickname from UserFollow as f inner join User as u where f.followerId = ? AND f.status = 1 AND f.followingId IN (Select followerId from UserFollow where followingId = ? and status = 1 and followerId IN (select userId from User where accountStatus = 'ACTIVE' and userId != ?)) and u.userId = f.followingId limit 1";
        Object[] getConnectedFriedNicknameParams = new Object[]{userIdByJwt, userId, userIdByJwt};
        return this.jdbcTemplate.queryForObject(getConnectedFriendNicknameQuery,
                (rs,rowNum)->rs.getString("u.nickname"),
        getConnectedFriedNicknameParams);
    }

    public int checkIfFollowing(int onlineUserId, int findingUserId) {
        String checkIfFollowingQuery = "select exists(select followingId from UserFollow where followerId = ? and followingId = ? and status = 1)";
        Object[] checkIfFollowingParams = new Object[]{onlineUserId,findingUserId};
        return this.jdbcTemplate.queryForObject(checkIfFollowingQuery,
                int.class,
                checkIfFollowingParams);
    }
}
