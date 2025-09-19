package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.common.RedisUtil;
import com.BallaDream.BallaDream.constants.EmailAuthNumberType;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.DuplicateIdException;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static com.BallaDream.BallaDream.constants.EmailAuthNumberType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String emailId;

    public boolean CheckAuthNum(String email, String authNum){

        if(redisUtil.getData(email)==null){
            log.info("not have key");
            return false;
        }
        else if(redisUtil.getData(email).equals(authNum)){
            log.info("correct key");
            return true;
        }
        else{
            log.info("not correct key");
            return false;
        }
    }

    //임의의 6자리 양수를 반환합니다.
    public int makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        return Integer.parseInt(randomNumber);
    }


    //회원가입을 위한 이메일 전송
    public String mailSendByType(String username, EmailAuthNumberType authNumberType) {

        Optional<User> findUser = userRepository.findByUsername(username);
        //가입용 이메일 전송을 하는 경우 이미 회원가입된 회원인지 확인
        if (findUser.isPresent() && authNumberType == JOIN_NUMBER) {
            //계정이 활성화 되어 있는 계정이라면 메일 전송을 하지 않는다.
            if(findUser.get().isEnabled()){
                throw new DuplicateIdException();
            }
        }

        int authNumber = makeRandomNumber();//인증번호 만들기
        String setFrom = emailId; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = username; //이메일을 받는 유저
        String title = "[BallaDream] 이메일 인증번호 입니다."; // 이메일 제목
        String content = getEmailContent(authNumber);
        mailSend(setFrom, toMail, title, content);
        setAuthNumberInRedis(toMail, authNumber); //5분동안 인증번호 정보 저장
        return Integer.toString(authNumber);
    }

    //이메일을 전송합니다.
    private void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
//        redisUtil.deleteData(toMail); //기존에 있는 인증 번호 삭제
//        redisUtil.setDataExpire(toMail, Integer.toString(authNumber),60*5L); //5분 동안 인증 번호 살아 있음
    }

    private void setAuthNumberInRedis(String toMail ,int authNumber) {
        redisUtil.deleteData(toMail); //기존에 있는 인증 번호 삭제
        redisUtil.setDataExpire(toMail, Integer.toString(authNumber),60*5L); //5분 동안 인증 번호 살아 있음
    }

    private String getEmailContent(int authNumber) {
        return """
        <div style="font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', '맑은 고딕', sans-serif; background-color: #f8f9fa; padding: 24px;">
            <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 32px 24px; border-radius: 10px; border: 1px solid #eee; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                <hr style="background-color: #162A6C; height: 2px; border: none; margin-bottom: 16px;" />
                <img src="https://balladream.shop/img/logo.png" alt="BallaDream 로고" style="width: 60px; margin-bottom: 16px;" />
                <h2 style="font-size: 20px; color: #162A6C; margin-bottom: 8px;">이메일 인증번호</h2>
                <p style="font-size: 15px; line-height: 1.6; color: #555;">
                    BallaDream 서비스의 이메일 인증을 진행합니다.<br/>
                    아래의 인증번호를 입력해 주세요.
                </p>
                <div style="margin: 32px 0; text-align: center;">
                    <div style="display: inline-block; padding: 16px 32px; background-color: #162A6C; color: white; font-size: 28px; font-weight: bold; border-radius: 8px; letter-spacing: 4px;">
                        %s
                    </div>
                </div>
                <p style="font-size: 14px; color: #999;">
                    본 이메일의 인증번호는 5분간 유효하며, 타인과 공유하지 마세요.
                </p>
                <hr style="background-color: #162A6C; height: 2px; border: none; margin-top: 24px;" />
            </div>
            <p style="margin-top: 16px; font-size: 12px; color: #ccc; text-align: center;">
                © 2025 BallaDream. All rights reserved.
            </p>
        </div>
        """.formatted(authNumber);
    }
}
