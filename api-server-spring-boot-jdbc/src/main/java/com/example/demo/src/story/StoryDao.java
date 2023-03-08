package com.example.demo.src.story;

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
        String checkStoryQuery = "select exists(select userStoryId from UserStory where userId = ?)";
        int checkStoryParams = userId;
        return this.jdbcTemplate.queryForObject(checkStoryQuery,
                int.class,
                checkStoryParams);
    }

}
