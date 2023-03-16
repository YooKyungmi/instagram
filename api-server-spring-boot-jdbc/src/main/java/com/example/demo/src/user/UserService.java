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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    @Transactional
    public PostUserRes createUserByPhone(PostUserByPhoneReq postUserByPhoneReq) throws BaseException {
        if (userProvider.checkPhoneNumber(postUserByPhoneReq.getPhoneNumber()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }
        if (userProvider.checkNickname(postUserByPhoneReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }
        throwIfInvalidBirthDateFormat(postUserByPhoneReq.getBirthDate());
        String pwd;
        try {
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserByPhoneReq.getPassword());
            postUserByPhoneReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userId = userDao.createUserByPhone(postUserByPhoneReq);
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt, userId);
        } catch (Exception exception) {
            logger.error("App - createUserByPhone Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional
    public PostUserRes createUserByEmail(PostUserByEmailReq postUserByEmailReq) throws BaseException {
        if (userProvider.checkEmailAddress(postUserByEmailReq.getEmailAddress()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL_ADDRESS);
        }
        if (userProvider.checkNickname(postUserByEmailReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }
        throwIfInvalidBirthDateFormat(postUserByEmailReq.getBirthDate());
        String pwd;
        try {
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserByEmailReq.getPassword());
            postUserByEmailReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userId = userDao.createUserByEmail(postUserByEmailReq);
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt, userId);
        } catch (Exception exception) {
            logger.error("App - createUserByEmail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidBirthDateFormat(String birthDate) throws BaseException {
        try {
            if (LocalDate.parse(birthDate).isAfter(LocalDate.now())) {
                throw new BaseException(POST_USERS_INVALID_BIRTH_DATE);
            }
        } catch (DateTimeParseException DTPE) {
            throw new BaseException(POST_USERS_INVALID_BIRTH_DATE_FORMAT);
        }
    }

    public void modifyUserInfo(PatchUserReq patchUserReq) throws BaseException {
        try {
            int result = userDao.updateUserInfo(patchUserReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception) {
            logger.error("App - modifyUserInfo Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserAccountStatus(int userId, String accountStatus) throws BaseException {
        try {
            int result = userDao.updateUserAccountStatus(userId, accountStatus);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception) {
            logger.error("App - modifyUserAccountStatus Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyUserEmail(PatchUserEmailReq patchUserEmailReq) throws BaseException {
        if (userProvider.checkEmailAddress(patchUserEmailReq.getEmailAddress()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL_ADDRESS);
        }
        try {
            int result = userDao.updateUserEmail(patchUserEmailReq.getUserId(), patchUserEmailReq.getEmailAddress());
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception) {
            logger.error("App - modifyUserEmail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyUserPhone(PatchUserPhoneReq patchUserPhoneReq) throws BaseException {
        if (userProvider.checkPhoneNumber(patchUserPhoneReq.getPhoneNumber()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }
        try {
            int result = userDao.updateUserPhone(patchUserPhoneReq.getUserId(), patchUserPhoneReq.getPhoneNumber());
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception) {
            logger.error("App - modifyUserEmail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfileImage(PatchProfileImageReq patchProfileImageReq) throws BaseException {
        try {
            int result = userDao.modifyProfileImage(patchProfileImageReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception) {
            logger.error("App - modifyProfileImage Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
