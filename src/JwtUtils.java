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

        int userId = UserManagerDAO.getUserId(userName);
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

    public static AuthInfo authJWT(Claims claims) {

        if (null == claims) {
            return new AuthInfo("did not get claims");
        }

        final String subject  = claims.getSubject();
        final String issuer   = claims.getIssuer();

        if (null == subject || SUBJECT != subject
                || null == issuer || ISSUER != issuer) {
            return new AuthInfo("subject or issuer is null");
        }

        Date now = new Date();
        final Date issueTime  = claims.getIssuedAt();
        final Date expireTime = claims.getExpiration();

        if (null == issueTime || null == expireTime) {
            return new AuthInfo("time is null");
        }

        if (expireTime.getTime() < now.getTime()
                || issueTime.getTime() > now.getTime()) {
            return new AuthInfo("Expire!");
        }

        final String userId   = (String)claims.get("id");
        final String userName = (String)claims.get("name");

        if (null == userId || null == userName) {
            return new AuthInfo("user is null");
        }

        int id = Integer.parseInt(userId);
        if (UserManagerDAO.getUserId(userName) != id
                && id <= 0) {
            return new AuthInfo("name mismatch id");
        }

        return new AuthInfo(userName, null);
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

class AuthInfo {
    String mUserName;
    String mErrorInfo;

    AuthInfo(String errorInfo) {
        mUserName = null;
        mErrorInfo = errorInfo;
    }

    AuthInfo(String userName, String errorInfo) {
        mUserName = userName;
        mErrorInfo = errorInfo;
    }
};
