package com.BallaDream.BallaDream.controller.user;

import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.jwt.JWTUtil;
import com.BallaDream.BallaDream.service.user.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.BallaDream.BallaDream.constants.TokenType.*;

@RequiredArgsConstructor
@RestController
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {

        String newAccessToken = reissueService.reissueAccessToken(request);

        response.setHeader("access", newAccessToken);
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "access 토큰이 재발급되었습니다."));
    }


    /*@PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh tokenz
        String refresh = reissueService.getTokenInCookie(request);
        //토큰이 존재하지 않는 경우
        if (refresh == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(HttpStatus.BAD_REQUEST.value(), "refresh 토큰을 전송해야 합니다."));
        }

        //토큰이 만료된 경우 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(HttpStatus.BAD_REQUEST.value(), "refresh 토큰이 만료되었습니다."));
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(REFRESH_TOKEN.getType())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 refresh 토큰입니다."));
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt(ACCESS_TOKEN, username, role, jwtUtil.getAccessTokenExpiredTime());

        //response
        response.setHeader("access", newAccess);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
