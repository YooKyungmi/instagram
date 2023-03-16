package com.example.demo.src.message;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MessageProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageDao messageDao;

    public MessageProvider(MessageDao messageDao) {
        this.messageDao = messageDao;
    }


    public int checkCode(String phoneNumber, String code) throws BaseException {
        try{
            return messageDao.checkCodeExist(phoneNumber,code);
        }catch (Exception exception){
            logger.error("App - checkCode Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try{
            return messageDao.checkPhoneNumber(phoneNumber);
        }catch (Exception exception){
            logger.error("App - checkPhoneNumber Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
