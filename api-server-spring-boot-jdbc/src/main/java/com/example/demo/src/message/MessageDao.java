package com.example.demo.src.message;

import com.example.demo.src.message.model.GetCodeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MessageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int saveCode(GetCodeRes getCodeRes) {
        String saveCodeQuery = "insert into PhoneCert (phoneNumber, authCode) VALUES (?,?)";
        Object[] saveCodeParams = new Object[]{getCodeRes.getPhoneNumber(),getCodeRes.getAuthCode()};
        return this.jdbcTemplate.update(saveCodeQuery,saveCodeParams);
    }

    public int checkCodeExist(String phoneNumber, String code) {
        String checkCodeExistQuery = "select exists(select authCode from PhoneCert where phoneNumber = ? and authCode = ?)";
        Object[] checkCodeExistParams = new Object[]{phoneNumber,code};
        return this.jdbcTemplate.queryForObject(checkCodeExistQuery,
                int.class,
                checkCodeExistParams);
    }

    public int deleteCode(String phoneNumber, String code) {
        String deleteCodeQuery = "delete from PhoneCert where phoneNumber = ? and authCode = ?";
        Object[] deleteCodeParams = new Object[]{phoneNumber, code};
        return this.jdbcTemplate.update(deleteCodeQuery,deleteCodeParams);
    }

    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneNumberQuery = "select exists(select authCode from PhoneCert where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);
    }

    public int deleteCodeForPhoneNumber(String phoneNumber) {
        String deleteCodeForPhoneNumberQuery = "delete from PhoneCert where phoneNumber = ?";
        String deleteCodeForPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.update(deleteCodeForPhoneNumberQuery,deleteCodeForPhoneNumberParams);
    }
}

