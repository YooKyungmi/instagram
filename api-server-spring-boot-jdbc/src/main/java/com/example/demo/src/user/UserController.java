package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String defaultProfileImageUrl = "아직 이미지 url 추출 방식 미정";

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 전화번호로 회원가입 API
     * [POST] /app/users/phone
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/phone")
    public BaseResponse<PostUserRes> createUserByPhone(@RequestBody PostUserByPhoneReq postUserByPhoneReq) {
        if (postUserByPhoneReq.getPhoneNumber() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if(postUserByPhoneReq.getPhoneNumber()!=null && !isRegexPhoneNumber(postUserByPhoneReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        if(postUserByPhoneReq.getBirthDate()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTH_DATE);
        }
        if(!isRegexBirthDate(postUserByPhoneReq.getBirthDate())){
            return new BaseResponse<>(POST_USERS_INVALID_BIRTH_DATE);
        }
        if(postUserByPhoneReq.getNickname()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        if(!isRegexNickname(postUserByPhoneReq.getNickname())){
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }
        if(postUserByPhoneReq.getPassword()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(!isRegexPassword(postUserByPhoneReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if(postUserByPhoneReq.getProfileImageUrl()==null){
            postUserByPhoneReq.setProfileImageUrl(defaultProfileImageUrl);
        }
        try {
            PostUserRes postUserRes = userService.createUserByPhone(postUserByPhoneReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이메일로 회원가입 API
     * [POST] /app/users/phone
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/email")
    public BaseResponse<PostUserRes> createUserByEmail(@RequestBody PostUserByEmailReq postUserByEmailReq) {
        if (postUserByEmailReq.getEmailAddress() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL_ADDRESS);
        }
        if (postUserByEmailReq.getEmailAddress() != null && !isRegexEmailAddress(postUserByEmailReq.getEmailAddress())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL_ADDRESS);
        }
        if(postUserByEmailReq.getBirthDate()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTH_DATE);
        }
        if(!isRegexBirthDate(postUserByEmailReq.getBirthDate())){
            return new BaseResponse<>(POST_USERS_INVALID_BIRTH_DATE);
        }
        if(postUserByEmailReq.getNickname()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        if(!isRegexNickname(postUserByEmailReq.getNickname())){
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }
        if(postUserByEmailReq.getPassword()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(!isRegexPassword(postUserByEmailReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if(postUserByEmailReq.getProfileImageUrl()==null){
            postUserByEmailReq.setProfileImageUrl(defaultProfileImageUrl);
        }
        try {
            PostUserRes postUserRes = userService.createUserByEmail(postUserByEmailReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /app/users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        if(postLoginReq.getId()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_ID);
        }
        if(postLoginReq.getPassword()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        try{
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
