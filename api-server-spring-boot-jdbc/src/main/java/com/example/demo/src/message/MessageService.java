package com.example.demo.src.message;

import com.example.demo.config.BaseException;
import com.example.demo.src.message.model.GetCodeRes;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.secret.Secret.COOL_SMS_API_KEY;
import static com.example.demo.config.secret.Secret.COOL_SMS_API_SECRET_KEY;

@Service
public class MessageService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageDao messageDao;

    private final String senderPhoneNumber= "01049109262";

    private final Message coolsms = new Message(COOL_SMS_API_KEY,COOL_SMS_API_SECRET_KEY);

    @Autowired
    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void certificatePhoneNumber(String phoneNumber, String authCode) throws BaseException {
        HashMap<String,String>params = new HashMap<String,String>();
        params.put("to",phoneNumber);
        params.put("from",senderPhoneNumber);
        params.put("type","SMS");
        params.put("text","[인스타그램 본인확인]본인확인 인증번호는 ["+authCode+"]입니다. 정확히 입력해주세요.");
        try{
            coolsms.send(params);
        } catch (CoolsmsException exception) {
            logger.error("App - certificatePhoneNumber Service Error", exception);
            throw new BaseException(FAILED_TO_SEND_MESSAGE);
        }
    }

    public void saveCode(GetCodeRes getCodeRes) throws BaseException {
        try{
            int result = messageDao.saveCode(getCodeRes);
            if(result == 0){
                throw new BaseException(FAILED_TO_SAVE_CODE);
            }
        } catch (BaseException exception) {
            logger.error("App - saveCode Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCode(String phoneNumber, String code) throws BaseException {
        try{
            int result = messageDao.deleteCode(phoneNumber,code);
            if(result == 0){
                throw new BaseException(FAILED_TO_DELETE_CODE);
            }
        }catch (BaseException exception) {
            logger.error("App - deleteCode Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCodeForPhoneNumber(String phoneNumber) throws BaseException {
        try{
            int result = messageDao.deleteCodeForPhoneNumber(phoneNumber);
            if(result == 0){
                throw new BaseException(FAILED_TO_DELETE_CODE);
            }
        }catch (BaseException exception) {
            logger.error("App - deleteCodeForPhoneNumber Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
