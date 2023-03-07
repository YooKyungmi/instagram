package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkEmailAddress(String email) throws BaseException {
        try {
            return userDao.checkEmailAddress(email);
        } catch (Exception exception) {
            logger.error("App - checkEmailAddress Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try {
            return userDao.checkPhoneNumber(phoneNumber);
        } catch (Exception exception) {
            logger.error("App - checkPhoneNumber Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickName) throws BaseException {
        try {
            return userDao.checkNickname(nickName);
        } catch (Exception exception) {
            logger.error("App - checkNickname Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        if (checkNickname(postLoginReq.getId()) == 0 && checkPhoneNumber(postLoginReq.getId()) == 0 && checkEmailAddress(postLoginReq.getId()) == 0) {
            throw new BaseException(POST_USERS_ID_NOT_EXIST);
        }
        User user = new User();
        try {
            user = userDao.findUserById(postLoginReq.getId());
        } catch (Exception exception) {
            logger.error("App - login Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception exception) {
            logger.error("App - login Provider Decrypt Error", exception);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if (postLoginReq.getPassword().equals(password)) {
            throwIfInvalidUserStatus(user);
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    private void throwIfInvalidUserStatus(User user) throws BaseException {
        if (user.getAccountStatus().equals("INACTIVE")) {
            throw new BaseException(POST_USERS_ACCOUNT_INACTIVE);
        }
        if (user.getAccountStatus().equals("DELETED")) {
            throw new BaseException(POST_USERS_ACCOUNT_DELETED);
        }

    }

}
