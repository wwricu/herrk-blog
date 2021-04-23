package util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dao.UserManagerDAO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtUtils {


    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 1;  //Expire time, millisecond, 1day
    public static final String SECRET = "SHENGTONG";
    public static final String ISSUER = "WANG.WEI.RAN@OUTLOOK.COM";
    public static final String SUBJECT = "Token for wangweiran.space";


    /**
     * generate jwt
     * @param userName
     * @return
     */
    public static String geneJsonWebToken(String userName) {

        if(userName == null) {
            return null;
        }

        int userId = UserManagerDAO.getUserInfo(userName).mUserId;
        if (userId <= 0) {
            return null;
        }

        String token = Jwts.builder()
                .claim("id", userId)
                .claim("name", userName)
                .setSubject(SUBJECT)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

        return token;
    }

    /**
     * Auth JWT
     * @param String token
     * @return
     */
    public static UserInfo authJWT(String token) {

        Claims claims = parseJWT(token);
        if (null == claims) {
            return null;
        }

        final String subject  = claims.getSubject();
        final String issuer   = claims.getIssuer();

        if (null == subject || false == subject.equals(SUBJECT)
                || null == issuer || false == issuer.equals(ISSUER)) {
            return null;
        }

        Date now = new Date();
        final Date issueTime  = claims.getIssuedAt();
        final Date expireTime = claims.getExpiration();

        if (null == issueTime || null == expireTime || null == now) {
            return null;
        }

        if (expireTime.getTime() < now.getTime()
                || issueTime.getTime() > now.getTime()) {
            return null;
        }

        final int userId   = (int)claims.get("id");
        final String userName = (String)claims.get("name");

        if (null == userName || userId <= 0) {
            return null;
        }

        if (UserManagerDAO.getUserInfo(userName).mUserId != userId) {
            return null;
        }

        return new UserInfo(userId, userName);
    }

    /**
     * parse token
     * @param token
     * @return
     */
    protected static Claims parseJWT(String token) {

        Claims claims;
        try {
            claims  = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        } catch (Exception e) {
            claims = null;
            e.printStackTrace();
        }

        return claims;
    }

};
