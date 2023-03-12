package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String defaultProfileImageUrl = "https://trudylin.s3.ap-northeast-2.amazonaws.com/postPhoto/profile+Image.png";


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
            return new BaseResponse<>(POST_USERS_INVALID_BIRTH_DATE_FORMAT);
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
            return new BaseResponse<>(POST_USERS_INVALID_BIRTH_DATE_FORMAT);
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

    /**
     * 회원 조회 API
     * [GET] /app/users/:user-id
     *
     * @return BaseResponse<List<GetUserRes>>
     */
    @GetMapping("/{user-id}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("user-id") Integer userId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            GetUserRes getUsersRes = userProvider.getUser(userIdByJwt, userId);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 기본 정보 수정 API
     * [PATCH] /app/users/profiles/:user-id
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/profiles/{user-id}")
    public BaseResponse<String> modifyUserInfo(@PathVariable("user-id") int userId, @RequestBody PatchUserReq patchUserReq){
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            patchUserReq.setUserId(userId);
            if(patchUserReq.getGender()==null){
                patchUserReq.setGender("UNDISCLOSED");
            }
            userService.modifyUserInfo(patchUserReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 상태 변경 API
     * [PATCH] /app/users/accounts/:user-id?account-status=
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/accounts/{user-id}")
    public BaseResponse<String> modifyUserStatus(@PathVariable("user-id") int userId, @RequestParam("account-status")String accountStatus){
        if(accountStatus==null){
            return new BaseResponse<>(PATCH_USERS_EMPTY_ACCOUNT_STATUS);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.modifyUserAccountStatus(userIdByJwt, accountStatus);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호로 유저 본인확인 API
     * [GET] /app/users/identifications/:userId
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping("/identifications/{user-id}")
    public BaseResponse<PostLoginRes> identifyUser(@PathVariable("user-id") int userId, @RequestBody GetIdentifyUserReq getIdentifyUserReq) {
        if(getIdentifyUserReq.getPassword()==null){
            return new BaseResponse<>(GET_USERS_EMPTY_PASSWORD);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            getIdentifyUserReq.setUserId(userId);
            PostLoginRes postLoginRes = userProvider.identifyUser(getIdentifyUserReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 이메일 추가/수정 API
     * [PATCH] /app/users/user-infos/email-addresses/:user-id
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/user-infos/email-addresses/{user-id}")
    public BaseResponse<String> modifyUserEmail(@PathVariable("user-id") int userId, @RequestBody PatchUserEmailReq patchUserEmailReq){
        if(patchUserEmailReq.getEmailAddress()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL_ADDRESS);
        }
        if (patchUserEmailReq.getEmailAddress() != null && !isRegexEmailAddress(patchUserEmailReq.getEmailAddress())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL_ADDRESS);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            patchUserEmailReq.setUserId(userId);
            userService.modifyUserEmail(patchUserEmailReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 전화번호 추가/수정 API
     * [PATCH] /app/users/user-infos/phone-numbers/:user-id
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/user-infos/phone-numbers/{user-id}")
    public BaseResponse<String> modifyUserEmail(@PathVariable("user-id") int userId, @RequestBody PatchUserPhoneReq patchUserPhoneReq){
        if(patchUserPhoneReq.getPhoneNumber()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if(patchUserPhoneReq.getPhoneNumber()!=null && !isRegexPhoneNumber(patchUserPhoneReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            patchUserPhoneReq.setUserId(userId);
            userService.modifyUserPhone(patchUserPhoneReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 닉네임으로 검색 API
     * [GET] /app/users?user-nickname=
     *
     * @return BaseResponse<List<GetUserSearchRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserSearchRes>> searchByUser(@RequestParam("user-keyword") String keyword) {
        if(keyword ==null){
            return new BaseResponse<>(GET_USERS_EMPTY_NICKNAME);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetUserSearchRes> getUserSearchResList = userProvider.searchByUser(userIdByJwt, keyword);
            return new BaseResponse<>(getUserSearchResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
