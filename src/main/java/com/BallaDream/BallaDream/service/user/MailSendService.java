package com.BallaDream.BallaDream.service.user;

import com.BallaDream.BallaDream.common.RedisUtil;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private int authNumber;

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
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }


    //회원가입을 위한 이메일 전송
    public String joinEmail(String username) {

        Optional<User> findUser = userRepository.findByUsername(username);
        //이미 회원가입된 회원이고
        if (findUser.isPresent()) {
            //계정이 활성화 되어 있는 계정이라면 메일 전송을 하지 않는다.
            if(findUser.get().isEnabled()){
                throw new DuplicateIdException();
            }
        }

        makeRandomNumber(); //인증번호 만들기
        String setFrom = emailId; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = username; //이메일을 받는 유저
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "balladream 을 방문해주셔서 감사합니다." + 	//html 형식으로 작성
                        "<br><br>" +
                        "인증 번호는 " + authNumber + " 입니다.";
        mailSend(setFrom, toMail, title, content);
        setAuthNumberInRedis(toMail, authNumber); //5분동안 인증번호 정보 저장
        return Integer.toString(authNumber);
    }

    //비밀번호를 찾기 위한 이메일 전송
    public String findPassword(String email, String password) {
        makeRandomNumber();
        String setFrom = emailId; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "비밀번호 찾기 이메일 입니다."; // 이메일 제목
        String content =
                "balladreamd을 방문해주셔서 감사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "비밀번호는 " + password + " 입니다.";
        mailSend(setFrom, toMail, title, content);
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

    private void setAuthNumberInRedis(String toMail ,int authNumberInRedis) {
        redisUtil.deleteData(toMail); //기존에 있는 인증 번호 삭제
        redisUtil.setDataExpire(toMail, Integer.toString(authNumber),60*5L); //5분 동안 인증 번호 살아 있음
    }
}
