package com.example.demo.src.highlight;

import com.example.demo.src.highlight.model.PostHighlightReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class HighlightDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUserHighlight(PostHighlightReq postHighlightReq) {
        String createHighlightQuery = "insert into UserHighlight (userId, title, coverImgUrl) VALUES (?,?,?)";
        Object[] createHighlightParams = new Object[]{postHighlightReq.getUserId(),postHighlightReq.getTitle(), postHighlightReq.getCoverImgUrl()};
        this.jdbcTemplate.update(createHighlightQuery, createHighlightParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createHighlight(int userHighlightId, int storyId) {
        String createHighlightQuery = "insert into Highlight (userHighlightId, userStoryId) VALUES (?,?)";
        Object[] createHighlightParams = new Object[]{userHighlightId,storyId};
        this.jdbcTemplate.update(createHighlightQuery, createHighlightParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }
}
