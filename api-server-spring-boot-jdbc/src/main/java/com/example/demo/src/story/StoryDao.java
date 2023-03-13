package com.example.demo.src.story;

import com.example.demo.src.story.model.GetStoryHistoryRes;
import com.example.demo.src.story.model.GetStoryRes;
import com.example.demo.src.story.model.GetStoryViewerRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // and TIMEDIFF(current_timestamp,updatedAt) < '24:00:00'
    public int checkStory(int userId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userId = ? and status = 1)";
        int checkStoryParams = userId;
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    //and TIMEDIFF(current_timestamp,updatedAt) < '24:00:00'
    public int checkStoryNotViewedExist(int userId, int onlineUserId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userStoryId = (select userStoryId from UserStory where userId = ? and status = 1 limit 1) and userStoryId NOT IN (select userStoryId from StoryViewer where userId = ?))";
        Object[] checkStoryParams = new Object[]{userId, onlineUserId};
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    // and TIMEDIFF(current_timestamp,updatedAt) < '24:00:00'
    public StoryProvider.StoryUser getStoryUsersInfo(int userId, int selfStatus, int viewStatus) {
        String getStoryUsersInfoQuery = "select s.updatedAt, s.userId, u.nickname, u.profileImageUrl from UserStory as s inner join User as u where userStoryId = (select userStoryId from UserStory where userId = ? and status = 1 order by updatedAt desc limit 1) and u.userId=s.userId";
        int getStoryUsersInfoParams = userId;
        return this.jdbcTemplate.queryForObject(getStoryUsersInfoQuery,
                (rs, rowNum) -> StoryProvider.StoryUser.builder()
                        .userId(rs.getInt("s.userId"))
                        .nickname(rs.getString("u.nickname"))
                        .profileImageUrl(rs.getString("u.profileImageUrl"))
                        .updatedAt(rs.getTimestamp("s.updatedAt").toString())
                        .selfStatus(selfStatus)
                        .viewStatus(viewStatus)
                        .build(),
                getStoryUsersInfoParams);

    }

    // and TIMEDIFF(current_timestamp,s.updatedAt)<'24:00:00'
    public List<GetStoryRes> getStoryByUserId(int userId) {
        String getStoryByUserIdQuery = "select s.*, nickname, profileImageUrl from UserStory as s inner join User as u where s.userId = ? and s.status = 1 and u.userId = s.userId order by s.updatedAt";
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
        String getStoryViewerCountQuery = "select Count(userId) as userIdCount from StoryViewer where userStoryId = ? and userId IN (select userId from User where accountStatus = 'ACTIVE')";
        int getStoryViewerCountParams = userStoryId;
        return this.jdbcTemplate.queryForObject(getStoryViewerCountQuery,
                (rs, rowNum) -> rs.getInt("userIdCount"),
                getStoryViewerCountParams);
    }

    public List<String> getStoryViewerProfileImageUrls(int userStoryId) {
        String getStoryViewerProfileImageUrlsQuery = "select profileImageUrl from StoryViewer as s inner join User as u where s.userStoryId = ? and s.userId = u.userId limit 2";
        int getStoryViewerProfileImageUrlsParams = userStoryId;
        return this.jdbcTemplate.query(getStoryViewerProfileImageUrlsQuery,
                (rs, rowNum) -> rs.getString("profileImageUrl"),
                getStoryViewerProfileImageUrlsParams);
    }

    // and TIMEDIFF(current_timestamp,updatedAt)<'24:00:00'
    public int checkStoryId(int storyId) {
        String checkStoryIdQuery = "select exists(select userStoryId from UserStory where userStoryId=? and status = 1)";
        int checkStoryIdParams = storyId;
        return this.jdbcTemplate.queryForObject(checkStoryIdQuery,
                int.class,
                checkStoryIdParams);
    }

    public GetStoryRes getStoryByStoryId(int storyId) {
        String getStoryByStoryIdQuery = "select s.*, nickname, profileImageUrl from UserStory as s inner join User as u where s.userStoryId = ? and u.userId = s.userId order by s.updatedAt";
        int getStoryByStoryIdParams = storyId;
        return this.jdbcTemplate.queryForObject(getStoryByStoryIdQuery,
                (rs, rowNum) -> GetStoryRes.builder()
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
        Object[] addStoryViewerParams = new Object[]{userStoryId, userId};
        this.jdbcTemplate.update(addStoryViewerQuery, addStoryViewerParams);
    }

    public int checkStoryViewer(int userStoryId, int userId) {
        String checkStoryViewerQuery = "select exists(select storyViewerId from StoryViewer where userStoryId = ? and userId = ?)";
        Object[] checkStoryViewerParams = new Object[]{userStoryId, userId};
        return this.jdbcTemplate.queryForObject(checkStoryViewerQuery,
                int.class,
                checkStoryViewerParams);
    }

    public int getStoryUserByStoryId(int storyId) {
        String getStoryUserByStoryIdQuery = "select userId from UserStory where userStoryId = ?";
        int getStoryUserByStoryIdParams = storyId;
        return this.jdbcTemplate.queryForObject(getStoryUserByStoryIdQuery, int.class, getStoryUserByStoryIdParams);
    }

    public int patchStory(int status, int storyId) {
        String patchStoryQuery = "update UserStory set status = ? where userStoryId = ?";
        Object[] patchStoryParams = new Object[]{status, storyId};
        return this.jdbcTemplate.update(patchStoryQuery, patchStoryParams);
    }

    public List<GetStoryViewerRes> getStoryViewerInfo(int storyId) {
        String getStoryViewerInfoQuery = "select s.userId, CASE WHEN u.name IS NOT NULL then u.name ELSE u.nickname END as showName, u.profileImageUrl from User as u inner join StoryViewer as s where s.userId = u.userId and s.userStoryId = ?";
        int getStoryViewerInfoParams = storyId;
        return this.jdbcTemplate.query(getStoryViewerInfoQuery,
                (rs, rowNum) -> GetStoryViewerRes.builder()
                        .userId(rs.getInt("s.userId"))
                        .name(rs.getString("showName"))
                        .profileImageUrl(rs.getString("u.profileImageUrl"))
                        .build(),
                getStoryViewerInfoParams);
    }

    public int checkStoryViewerLikeStatus(int userId, int storyId) {
        String checkStoryViewerLikeStatusQuery = "select likeStatus from StoryViewer where userId = ? and userStoryId = ?";
        Object[] checkStoryViewerLikeStatusParams = new Object[]{userId, storyId};
        return this.jdbcTemplate.queryForObject(checkStoryViewerLikeStatusQuery,
                (rs, rowNum) -> rs.getInt("likeStatus"),
                checkStoryViewerLikeStatusParams);
    }

    public int patchStoryLikeStatus(int userId, int storyId, int likeStatus) {
        String patchStoryLikeStatusQuery = "update StoryViewer set likeStatus = ? where userId = ? and userStoryId = ?";
        Object[] patchStoryLikeStatusParams = new Object[]{likeStatus, userId, storyId};
        return this.jdbcTemplate.update(patchStoryLikeStatusQuery, patchStoryLikeStatusParams);
    }

    public List<GetStoryHistoryRes> getAllStories(int userId) {
        String getAllStoriesQuery = "select userStoryId, storyUrl, createdAt from UserStory where userId = ? and status = 1 order by createdAt";
        int getAllStoriesParams = userId;
        return this.jdbcTemplate.query(getAllStoriesQuery,
                (rs, rowNum) -> GetStoryHistoryRes.builder()
                        .userStoryId(rs.getInt("userStoryId"))
                        .storyUrl(rs.getString("storyUrl"))
                        .createdAt(rs.getTimestamp("createdAt").toString())
                        .build(),
                getAllStoriesParams);
    }
}
