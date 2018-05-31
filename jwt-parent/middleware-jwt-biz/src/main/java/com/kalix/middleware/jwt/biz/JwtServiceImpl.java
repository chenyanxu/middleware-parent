package com.kalix.middleware.jwt.biz;

import java.security.Key;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.api.jwt.IJwtService;
import com.kalix.framework.core.api.dto.AudienceBean;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtServiceImpl implements IJwtService {
    public Claims parseJWT(String jsonWebToken, String base64Security){
        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    public  String createJWT(String name, String userId, String role,
                                   String audience, String issuer, long TTLMillis, String base64Security)
    {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥  
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("unique_name", name)
                .claim("userid", userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }


    public AudienceBean getAudien()
    {
        AudienceBean audience = new AudienceBean();
        Dictionary<String, Object> config= ConfigUtil.getAllConfig("config.middleware.jwt.etc");
        Enumeration enumeration= config.keys();
        for(Enumeration e=enumeration;e.hasMoreElements();) {
            String keyName = e.nextElement().toString();
            if(!"felix.fileinstall.filename".equals(keyName)&&!"service.pid".equals(keyName))
            {
                String type=keyName.split("\\.")[0];
                String key=keyName.split("\\.")[1];
                String value=keyName.split("\\.")[2];
                if ("clientId".equals(value)) {
                    audience.setClientId(config.get(keyName).toString());
                }
                if ("base64Secret".equals(value)) {
                    audience.setBase64Secret(config.get(keyName).toString());
                }
                if ("name".equals(value)) {
                    audience.setName(config.get(keyName).toString());
                }
                if ("expiresSecond".equals(value)) {
                    audience.setExpiresSecond(Integer.parseInt(config.get(keyName).toString()));
                }
                if ("refresh_expiresSecond".equals(value)) {
                    audience.setRefresh_expiresSecond(Integer.parseInt(config.get(keyName).toString()));
                }
            }
        }
        return  audience;
    }

    public String refreshToken(String jsonWebToken)
    {
        AudienceBean audienceBean=this.getAudien();
        Claims claims= parseJWT(jsonWebToken,audienceBean.getBase64Secret());
        String role=(String)claims.get("role");
        String unique_name=(String)claims.get("unique_name");
        String userid=(String)claims.get("userid");

        String token=createJWT(unique_name,userid,role,audienceBean.getClientId(), audienceBean.getName(),
                audienceBean.getExpiresSecond() * 1000, audienceBean.getBase64Secret());

        return token;
    }

}  