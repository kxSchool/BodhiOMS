package com.penguin.website.util;



import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * @date Jan 14, 2019
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private static final String USERNAME = Claims.SUBJECT;

    private static final String USERID = Claims.ID;

    private static final String ISSUER = Claims.ISSUER;

    /**
     * 创建时间
     */
    private static final String CREATED = "created";

    /**
     * 密钥
     */
    private static final String SECRET = "penguins_shop";

    /**
     * 有效期12小时
     */
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;



    /**
     * 生成令牌
     *
     * @param user 用户
     * @return 令牌
     */
    public static String generateToken(PenguinsUser user) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(USERNAME, user.getUserName());
        claims.put(USERID, user.getUserId());
        claims.put(ISSUER, user.getUserToken());
        claims.put(CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成令牌
     *
     * @param user 用户
     * @return 令牌
     */
    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(USERNAME, user.getUserName());
        claims.put(USERID, user.getUserId());
        claims.put(CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            System.out.println(expiration + "====" + new Date());
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token中的用户id
     * @return
     */
    public static String getUserId(String token) {
        if(StringUtils.isNoneBlank(token)){
            Claims claims = getClaimsFromToken(token);
            if(claims != null){
                return claims.get(USERID, String.class);
            }
        }
        return null;
    }

    /**
     * 获取token中的用户发行人
     * @return
     */
    public static String getUserToken(String token) {
        if(StringUtils.isNoneBlank(token)){
            Claims claims = getClaimsFromToken(token);
            if(claims != null){
                return claims.get(ISSUER, String.class);
            }
        }
        return null;
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (token == null) {
            token = request.getHeader("token");
        } else if (token.contains(tokenHead)) {
            token = token.substring(tokenHead.length());
        }
        if ("".equals(token)) {
            token = null;
        }
        return token;
    }

    /**
     * 获取token中的用户名
     * @return
     */
    public static String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = getToken(request);
        if(StringUtils.isNoneBlank(token)){
            Claims claims = getClaimsFromToken(token);
            if(claims != null){
                return claims.get(USERNAME, String.class);
            }
        }
        return null;
    }
}
