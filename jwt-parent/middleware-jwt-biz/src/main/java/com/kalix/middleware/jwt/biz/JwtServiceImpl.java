package com.kalix.middleware.jwt.biz;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
import org.apache.commons.codec.binary.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

public class JwtServiceImpl implements IJwtService {
    private final String PrivateKey_File = "jwtRS256.key";
    private final String PublicKey_File = "jwtRS256.key.pub";
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

    public String createJWT(String name, String userId, String role,
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

    @Override
    public String getPrivateKeyString() {
        return getKeyString(PrivateKey_File);
    }

    @Override
    public String getPublicKeyString() {
        return getKeyString(PublicKey_File);
    }

    private String getKeyString(String keyFile) {
        String keyStr = FileUtil.loadFile(keyFile);
        if (keyStr != null) {
            keyStr = keyStr.replaceAll("\r", "");
            keyStr = keyStr.trim();
        }
        return keyStr;
    }

    /**
     * 获取PrivateKey对象
     * @param privateKeyBase64
     * @return
     */
    private PrivateKey getPrivateKey(String privateKeyBase64) {
        String privKeyPEM = privateKeyBase64.replaceAll("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll("\n", "");
        byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(privKeyPEM);
        try {
            DerInputStream derReader = new DerInputStream(encoded);
            DerValue[] seq = derReader.getSequence(0);
            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not read private key");
            }
            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger primeP = seq[4].getBigInteger();
            BigInteger primeQ = seq[5].getBigInteger();
            BigInteger expP = seq[6].getBigInteger();
            BigInteger expQ = seq[7].getBigInteger();
            BigInteger crtCoeff = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp,
                    primeP, primeQ, expP, expQ, crtCoeff);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpec);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        String pem = publicKeyBase64
                .replaceAll("\\-*BEGIN.*KEY\\-*", "")
                .replaceAll("\\-*END.*KEY\\-*", "");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        return publicKey;
    }

    @Override
    public String createJwt_RS256(String issuerKey, String userName, Long issuedAt) {
        String privateKey = getPrivateKeyString();
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("typ", "JWT")
                .claim("iss", issuerKey);
        if (userName != null && !userName.isEmpty()) {
            builder.claim("name", userName);
        }
        if (issuedAt != null) {
            builder.claim("iat", issuedAt);
        }
        builder.signWith(SignatureAlgorithm.RS256, getPrivateKey(privateKey));
        return builder.compact();
    }

    @Override
    public String createJwt_HS256(String secret, Boolean isSecretBase64, String issuerKey, String userName, Long issuedAt) {
        byte[] apiKeySecretBytes;
        if (isSecretBase64) {
            apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        } else {
            apiKeySecretBytes = secret.getBytes();
        }
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("iss", issuerKey);
        if (userName != null && !userName.isEmpty()) {
            builder.claim("name", userName);
        }
        if (issuedAt != null) {
            builder.claim("iat", issuedAt);
        }
        builder.signWith(SignatureAlgorithm.HS256, signingKey);
        return builder.compact();
    }

    @Override
    public Claims parseJwt_RS256(String jsonWebToken) {
        String publicKey = getPublicKeyString();
        try {
            Claims claims = Jwts.parser()
                   .setSigningKey(getPublicKey(publicKey))
                   .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}  