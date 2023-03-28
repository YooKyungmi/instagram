package com.example.demo.src.message;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.message.model.GetCertRes;
import com.example.demo.src.message.model.GetCodeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexCode;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/app/messages")
public class MessageController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MessageService messageService;
    @Autowired
    private final MessageProvider messageProvider;

    public MessageController(MessageService messageService, MessageProvider messageProvider) {
        this.messageService = messageService;
        this.messageProvider = messageProvider;
    }

    /**
     * 인증 문자 전송하기 API
     * [GET] /app/messages?phone-number=
     *
     * @return BaseResponse<GetCodeRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetCodeRes> sendMessage(@RequestParam("phone-number")String phoneNumber){
        if (phoneNumber == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if (phoneNumber != null && !isRegexPhoneNumber(phoneNumber)) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        try{
            if(messageProvider.checkPhoneNumber(phoneNumber)==1){
                messageService.deleteCodeForPhoneNumber(phoneNumber);
            }
            String authCode = createRandomNum();
            messageService.certificatePhoneNumber(phoneNumber,authCode);
            GetCodeRes getCodeRes = GetCodeRes.builder()
                    .phoneNumber(phoneNumber)
                    .authCode(authCode)
                    .build();
            messageService.saveCode(getCodeRes);
            return new BaseResponse<>(getCodeRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    private String createRandomNum() {
        Random generator = new Random();
        String random = "";
        for(int i=0;i<6;i++){
            String num = Integer.toString(generator.nextInt(10));
            random = random+num;
        }
        return random;
    }


    /**
     * 인증번호 입력받아 인증하기 API
     * [GET] /app/users/profile-images/:user-id
     *
     * @return BaseResponse<GetCertRes>
     */
    @ResponseBody
    @PostMapping("/{phone-number}")
    public BaseResponse<GetCertRes> sendMessage(@PathVariable("phone-number")String phoneNumber, @RequestParam("code")String code){
        if (phoneNumber == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if (phoneNumber != null && !isRegexPhoneNumber(phoneNumber)) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        if(code==null){
            return new BaseResponse<>(POST_MESSAGES_EMPTY_CODE);
        }
        if(!isRegexCode(code)){
            return new BaseResponse<>(POST_MESSAGES_INVALID_CODE);
        }
        try{
            GetCertRes getCertRes = GetCertRes.builder()
                    .phoneNumber(phoneNumber)
                    .authStatus(messageProvider.checkCode(phoneNumber,code))
                    .build();
            if(getCertRes.getAuthStatus()==1) {
                messageService.deleteCode(phoneNumber, code);
            }
            return new BaseResponse<>(getCertRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
