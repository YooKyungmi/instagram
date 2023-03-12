package com.example.demo.src.story;

import com.example.demo.src.story.model.GetStoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkStory(int userId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userId = ? and status = 1 and TIMEDIFF(current_timestamp,updatedAt) < '24:00:00')";
        int checkStoryParams = userId;
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    public int checkStoryNotViewedExist(int userId, int onlineUserId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userStoryId = (select userStoryId from UserStory where userId = ? and status = 1 and TIMEDIFF(current_timestamp,updatedAt)<'24:00:00' limit 1) and userStoryId NOT IN (select userStoryId from StoryViewer where userId = ?))";
        Object[] checkStoryParams = new Object[]{userId, onlineUserId};
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    public StoryProvider.StoryUser getStoryUsersInfo(int userId, int selfStatus, int viewStatus) {
        String getStoryUsersInfoQuery = "select s.updatedAt, s.userId, u.nickname, u.profileImageUrl from UserStory as s inner join User as u where userStoryId = (select userStoryId from UserStory where userId = ? and status = 1 and TIMEDIFF(current_timestamp,updatedAt)<'24:00:00' order by updatedAt desc limit 1) and u.userId=s.userId";
        int getStoryUsersInfoParams = userId;
        return this.jdbcTemplate.queryForObject(getStoryUsersInfoQuery,
                (rs,rowNum)-> StoryProvider.StoryUser.builder()
                        .userId(rs.getInt("s.userId"))
                        .nickname(rs.getString("u.nickname"))
                        .profileImageUrl(rs.getString("u.profileImageUrl"))
                        .updatedAt(rs.getTimestamp("s.updatedAt").toString())
                        .selfStatus(selfStatus)
                        .viewStatus(viewStatus)
                        .build(),
        getStoryUsersInfoParams);

    }

    public List<GetStoryRes> getStoryByUserId(int userId) {
        String getStoryByUserIdQuery = "select s.*, nickname, profileImageUrl from UserStory as s inner join User as u where s.userId = ? and s.status = 1 and TIMEDIFF(current_timestamp,s.updatedAt)<'24:00:00' and u.userId = s.userId order by s.updatedAt";
        int getStoryByUserIdParams = userId;
        return this.jdbcTemplate.query(getStoryByUserIdQuery,
                (rs, rowNum) -> GetStoryRes.builder()
                        .userStoryId(rs.getInt("s.userStoryId"))
                        .userId(rs.getInt("s.userId"))
                        .nickname(rs.getString("nickname"))
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .storyUrl(rs.getString("s.storyUrl"))
                        .createdAt(rs.getTimestamp("s.createdAt").toString())
                        .updatedAt(rs.getTimestamp("s.updatedAt").toString())
                        .build(),
        getStoryByUserIdParams);
    }

    public int getStoryViewerCount(int userStoryId) {
        String getStoryViewerCountQuery = "select Count(userId) as userIdCount from StoryViewer where userStoryId = ?";
        int getStoryViewerCountParams = userStoryId;
        return this.jdbcTemplate.queryForObject(getStoryViewerCountQuery,
                (rs,rowNum)->rs.getInt("userIdCount"),
                getStoryViewerCountParams);
    }

    public List<String> getStoryViewerProfileImageUrls(int userStoryId) {
        String getStoryViewerProfileImageUrlsQuery = "select profileImageUrl from StoryViewer as s inner join User as u where s.userStoryId = ? and s.userId = u.userId limit 2";
        int getStoryViewerProfileImageUrlsParams = userStoryId;
        return this.jdbcTemplate.query(getStoryViewerProfileImageUrlsQuery,
                (rs, rowNum) -> rs.getString("profileImageUrl"),
                getStoryViewerProfileImageUrlsParams);
    }

    public int checkStoryId(int storyId) {
        String checkStoryIdQuery = "select exists(select userStoryId from UserStory where userStoryId=? and status = 1 and TIMEDIFF(current_timestamp,updatedAt)<'24:00:00')";
        int checkStoryIdParams = storyId;
        return this.jdbcTemplate.queryForObject(checkStoryIdQuery,
                int.class,
                checkStoryIdParams);
    }

    public GetStoryRes getStoryByStoryId(int storyId) {
        String getStoryByStoryIdQuery = "select s.*, nickname, profileImageUrl from UserStory as s inner join User as u where s.userStoryId = ? and u.userId = s.userId order by s.updatedAt";
        int getStoryByStoryIdParams = storyId;
        return this.jdbcTemplate.queryForObject(getStoryByStoryIdQuery,
                (rs, rowNum)->GetStoryRes.builder()
                        .userStoryId(rs.getInt("s.userStoryId"))
                        .userId(rs.getInt("s.userId"))
                        .nickname(rs.getString("nickname"))
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .storyUrl(rs.getString("s.storyUrl"))
                        .createdAt(rs.getTimestamp("s.createdAt").toString())
                        .updatedAt(rs.getTimestamp("s.updatedAt").toString())
                        .build(),
                getStoryByStoryIdParams);
    }

    public void addStoryViewer(int userStoryId, int userId) {
        String addStoryViewerQuery = "insert into StoryViewer (userStoryId, userId) VALUES (?,?)";
        Object[] addStoryViewerParams = new Object[]{userStoryId,userId};
        this.jdbcTemplate.update(addStoryViewerQuery, addStoryViewerParams);
    }

    public int checkStoryViewer(int userStoryId, int userId) {
        String checkStoryViewerQuery = "select exists(select storyViewerId from StoryViewer where userStoryId = ? and userId = ?)";
        Object[] checkStoryViewerParams = new Object[]{userStoryId,userId};
        return this.jdbcTemplate.queryForObject(checkStoryViewerQuery,
                int.class,
                checkStoryViewerParams);
    }
}
