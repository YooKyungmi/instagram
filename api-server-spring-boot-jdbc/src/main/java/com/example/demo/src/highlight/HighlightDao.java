package com.example.demo.src.highlight;

import com.example.demo.src.highlight.model.GetHighlightByHighlightIdRes;
import com.example.demo.src.highlight.model.GetHighlightByUserIdRes;
import com.example.demo.src.highlight.model.PostHighlightReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    public int checkHighlightByUserId(int userId) {
        String checkHighlightByUserIdQuery = "select exists(select userHighlightId from UserHighlight where userId = ? and status = 1)";
        int checkHighlightByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkHighlightByUserIdQuery,
                int.class,
                checkHighlightByUserIdParams);
    }

    public List<GetHighlightByUserIdRes> getHighlightsByUserId(int userId) {
        String getHighlightsByUserIdQuery = "select userHighlightId, userId,title, coverImgUrl from UserHighlight where userId = ? and status = 1 order by updatedAt desc";
        int getHighlightsByUserIdParams = userId;
        return this.jdbcTemplate.query(getHighlightsByUserIdQuery,
                (rs,rowNum)->GetHighlightByUserIdRes.builder()
                        .userHighlightId(rs.getInt("userHighlightId"))
                        .userId(rs.getInt("userId"))
                        .title(rs.getString("title"))
                        .coverImgUrl(rs.getString("coverImgUrl"))
                        .build(),
        getHighlightsByUserIdParams);
    }

    public int checkHighlightByHighlightId(int highlightId) {
        String checkHighlightByHighlightIdQuery = "select exists(select userHighlightId from UserHighlight where userHighlightId = ? and status = 1)";
        int checkHighlightByHighlightIdParams = highlightId;
        return this.jdbcTemplate.queryForObject(checkHighlightByHighlightIdQuery,
                int.class,
                checkHighlightByHighlightIdParams);
    }

    public List<GetHighlightByHighlightIdRes> getAllStoriesByHighlightId(int highlightId) {
        String getAllStoriesByHighlightIdQuery = "select s.userId, s.storyUrl, s.userStoryId, s.createdAt, uh.userHighlightId, uh.title, uh.coverImgUrl from UserHighlight as uh inner join UserStory as s, Highlight as h where s.userStoryId IN (select userStoryId from Highlight where userHighlightId = ? and status = 1) and uh.userHighlightId = ? and uh.userHighlightId = h.userHighLightId and s.userStoryId = h.userStoryId and s.status = 1 order by s.createdAt";
        Object[] getAllStoriesByHighlightIdParams = new Object[]{highlightId, highlightId};
        return this.jdbcTemplate.query(getAllStoriesByHighlightIdQuery,
                (rs,rowNum)->GetHighlightByHighlightIdRes.builder()
                        .userHighlightId(rs.getInt("uh.userHighlightId"))
                        .userId(rs.getInt("s.userId"))
                        .storyId(rs.getInt("s.userStoryId"))
                        .title(rs.getString("uh.title"))
                        .coverImgUrl(rs.getString("uh.coverImgUrl"))
                        .storyUrl(rs.getString("s.storyUrl"))
                        .createdAt(rs.getTimestamp("s.createdAt").toString())
                        .build(),
                getAllStoriesByHighlightIdParams);
    }
}
