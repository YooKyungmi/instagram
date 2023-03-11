package com.example.demo.src.story;

import com.example.demo.src.story.model.GetStoryUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkStory(int userId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userId = ? and status = 1)";
        int checkStoryParams = userId;
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    public int checkStoryNotViewedExist(int userId, int onlineUserId) {
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userStoryId = (select userStoryId from UserStory where userId = ? and status = 1 and userStoryId NOT IN (select userStoryId from StoryViewer where userId = ?) order by updatedAt desc limit 1))";
        Object[] checkStoryParams = new Object[]{userId, onlineUserId};
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

    public GetStoryUserRes getStoryUsersInfo(int userId, int viewStatus) {
        String getStoryUsersInfoQuery = "select s.updatedAt, s.userId, u.nickname, u.profileImageUrl from UserStory as s inner join User as u where userStoryId = (select userStoryId from UserStory where userId = ? and status =1 order by updatedAt desc limit 1) and u.userId=s.userId";
        int getStoryUsersInfoParams = userId;
        return this.jdbcTemplate.queryForObject(getStoryUsersInfoQuery,
                (rs,rowNum)-> GetStoryUserRes.builder()
                        .userId(rs.getInt("s.userId"))
                        .nickname(rs.getString("u.nickname"))
                        .profileImageUrl(rs.getString("u.profileImageUrl"))
                        .updatedAt(rs.getTimestamp("s.updatedAt").toString())
                        .viewStatus(viewStatus)
                        .build()
        ,getStoryUsersInfoParams);

    }

}
