package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUserByPhone(PostUserByPhoneReq postUserByPhoneReq){
        String createUserQuery = "insert into User (phoneNumber, birthDate, nickname, password, profileImageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserByPhoneReq.getPhoneNumber(), postUserByPhoneReq.getBirthDate(), postUserByPhoneReq.getNickname(),postUserByPhoneReq.getPassword(), postUserByPhoneReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createUserByEmail(PostUserByEmailReq postUserByEmailReq){
        String createUserQuery = "insert into User (emailAddress, birthDate, nickname, password, profileImageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserByEmailReq.getEmailAddress(), postUserByEmailReq.getBirthDate(), postUserByEmailReq.getNickname(),postUserByEmailReq.getPassword(), postUserByEmailReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkEmailAddress(String email){
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
}
