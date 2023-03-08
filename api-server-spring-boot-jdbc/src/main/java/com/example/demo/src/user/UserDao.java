package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUserByPhone(PostUserByPhoneReq postUserByPhoneReq) {
        String createUserQuery = "insert into User (phoneNumber, birthDate, nickname, password, profileImageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserByPhoneReq.getPhoneNumber(), postUserByPhoneReq.getBirthDate(), postUserByPhoneReq.getNickname(), postUserByPhoneReq.getPassword(), postUserByPhoneReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createUserByEmail(PostUserByEmailReq postUserByEmailReq) {
        String createUserQuery = "insert into User (emailAddress, birthDate, nickname, password, profileImageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserByEmailReq.getEmailAddress(), postUserByEmailReq.getBirthDate(), postUserByEmailReq.getNickname(), postUserByEmailReq.getPassword(), postUserByEmailReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkEmailAddress(String email) {
        String checkEmailQuery = "select exists(select emailAddress from User where emailAddress = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);
    }

    public int checkNickname(String nickname) {
        String checkNicknameQuery = "select exists(select nickname from User where nickname = ?)";
        String checkNicknameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNicknameQuery,
                int.class,
                checkNicknameParams);
    }

    public User findUserById(String id) {
        String findUserQuery = "select * from User where phoneNumber = ? or emailAddress = ? or nickname = ?";
        Object[] findUserParams = new Object[]{id, id, id};
        return this.jdbcTemplate.queryForObject(findUserQuery,
                (rs, rowNum) -> User.builder()
                        .userId(rs.getInt("userId"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .emailAddress(rs.getString("emailAddress"))
                        .birthDate(rs.getString("birthDate").toString())
                        .nickname(rs.getString("nickname"))
                        .password(rs.getString("password"))
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .name(rs.getString("name"))
                        .introduce(rs.getString("introduce"))
                        .gender(rs.getString("gender"))
                        .accountStatus(rs.getString("accountStatus"))
                        .build(),
                findUserParams);
    }

    public User getUser(int userId) {
        String getUserQuery = "select * from User where userId = ?";
        int getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> User.builder()
                        .userId(rs.getInt("userId"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .emailAddress(rs.getString("emailAddress"))
                        .birthDate(rs.getString("birthDate").toString())
                        .nickname(rs.getString("nickname"))
                        .password(rs.getString("password"))
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .name(rs.getString("name"))
                        .introduce(rs.getString("introduce"))
                        .gender(rs.getString("gender"))
                        .accountStatus(rs.getString("accountStatus"))
                        .status(rs.getInt("status"))
                        .createdAt(rs.getTimestamp("createdAt").toString())
                        .updatedAt(rs.getTimestamp("updatedAt").toString())
                        .build(),
                getUserParams);
    }

    public int checkUserId(int userId) {
        String checkUserIdQuery = "select exists(select userId from User where userId = ?)";
        int checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);
    }

    public GetUserProfileRes getUserProfile(int userId) {
        String getUserQuery = "select nickname, profileImageUrl from User where userId=?";
        int getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserProfileRes(
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")),
                getUserParams);
    }
}
