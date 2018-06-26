package com.kalix.middleware.kongclient.biz;

import com.kalix.framework.core.api.jwt.IJwtService;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.middleware.kongclient.api.biz.IKongJwtService;
import com.kalix.middleware.kongclient.biz.kong.impl.KongClient;
import com.kalix.middleware.kongclient.biz.kong.model.admin.api.Api;
import com.kalix.middleware.kongclient.biz.kong.model.admin.consumer.Consumer;
import com.kalix.middleware.kongclient.biz.kong.model.admin.plugin.Plugin;
import com.kalix.middleware.kongclient.biz.kong.model.admin.plugin.PluginList;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt.JwtConfig;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredential;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredentialList;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.*;

public class KongJwtServiceImpl implements IKongJwtService {
    private KongClient kongClient;
    private String KONG_ADMIN_URL = null;
    private String KONG_PROXY_URL = null;
    private String KONG_AUTH2_API_URL = null;
    private String API_UPSTREAM_URL = null;
    private String API_URIS = null;
    private String API_NAME = null;
    private String API_METHODS = null;
    private String CONSUMER_NAME = null;
    private IJwtService jwtService;
    private String PUBLIC_KEY_START = "-----BEGIN PUBLIC KEY-----\n";
    private String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
    private String PUBLIC_KEY =
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA7U2fZN7+vHl13C3zrZ3t\n" +
            "WG5Oqj7aFI24IwLnO372mk88HLsS2R6dFd2niL55kSHy+S5bxy8CHGHaFAfcvgk9\n" +
            "UILEDXNLX7zMl/gTTkiQCRFR3dS7Pz30zc/cZfeb3gBmQdcoMVugLT+fp6qRN9Df\n" +
            "kdqYg0YYedJLqrz7XTfXom2myTnBFXsAdJxv0HbTH8xq+w3IBcjjTcDa+oXSxt2v\n" +
            "M90vGuqsNiVZPFa4OvyczYJpmbQ0pgtYgRRvocNxUXobKW6Vhw5WirR/cZKt/lLC\n" +
            "HwASph3983BcWgzt4XqoAfi5jE0S4MblDUbal1gTQQFDc4k7w2R8VwW41aCfpPmz\n" +
            "HUIkM8RPkEYrZzpStIYTAEAVrir43gSfwIUD+oDtqyoFNnv9i/RmeMSn2MWmQThd\n" +
            "4L9DMJfap7s0LM+hHBBuWSvP33oboAYm4r2C8naNbCkSO0NU25Uzt3+kbL67D7Yj\n" +
            "riNipQYx0BNVtNs6b50BJSOJYrF4TfWFIIg6mFzYLhnnm3zyrKCqti4rwGRC5QGE\n" +
            "Uwd2ZllfmsudX4MqyZ3v+LUgmezL2xQ3BfghlZP4p2IpRgF0/E6CfxwoPtwwVvUJ\n" +
            "f7VfGdDffBQ4V0QCWYAavdmo3wkYPLiMVbt1QEJvVmq200ha+piqIZ3llCErHNaS\n" +
            "ArxW7Vn1ihN2DTd1LlBgp48CAwEAAQ==\n";
//            "-----END PUBLIC KEY-----\n";
    private String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIJKQIBAAKCAgEA7U2fZN7+vHl13C3zrZ3tWG5Oqj7aFI24IwLnO372mk88HLsS\n" +
            "2R6dFd2niL55kSHy+S5bxy8CHGHaFAfcvgk9UILEDXNLX7zMl/gTTkiQCRFR3dS7\n" +
            "Pz30zc/cZfeb3gBmQdcoMVugLT+fp6qRN9DfkdqYg0YYedJLqrz7XTfXom2myTnB\n" +
            "FXsAdJxv0HbTH8xq+w3IBcjjTcDa+oXSxt2vM90vGuqsNiVZPFa4OvyczYJpmbQ0\n" +
            "pgtYgRRvocNxUXobKW6Vhw5WirR/cZKt/lLCHwASph3983BcWgzt4XqoAfi5jE0S\n" +
            "4MblDUbal1gTQQFDc4k7w2R8VwW41aCfpPmzHUIkM8RPkEYrZzpStIYTAEAVrir4\n" +
            "3gSfwIUD+oDtqyoFNnv9i/RmeMSn2MWmQThd4L9DMJfap7s0LM+hHBBuWSvP33ob\n" +
            "oAYm4r2C8naNbCkSO0NU25Uzt3+kbL67D7YjriNipQYx0BNVtNs6b50BJSOJYrF4\n" +
            "TfWFIIg6mFzYLhnnm3zyrKCqti4rwGRC5QGEUwd2ZllfmsudX4MqyZ3v+LUgmezL\n" +
            "2xQ3BfghlZP4p2IpRgF0/E6CfxwoPtwwVvUJf7VfGdDffBQ4V0QCWYAavdmo3wkY\n" +
            "PLiMVbt1QEJvVmq200ha+piqIZ3llCErHNaSArxW7Vn1ihN2DTd1LlBgp48CAwEA\n" +
            "AQKCAgEAgEj3Q0u+AFvVGFuLIS+AEM9JHYXu1S+r/Nlj8ej4HYF6KLoFPXTsTNbP\n" +
            "6/+0rn3g4U5rdSl3hapsf2mkruNFz1Fx9Sd/9tiU7g7BOQ/HNe+0By0qsMyom96j\n" +
            "8kfCbmhe33cWFmDHHc8IW1a9PKRhSHMZAdaSAsFqAHo7lTBMFVYb1SqwtBETtPah\n" +
            "B9xE1CrpJqFTobTxFKaOeuJ/iNxtC6emAHIe+QKF1DW5UMIhboPy3JHr6Fu308gd\n" +
            "dSC4EOjm6CYNFb7tj/x17QrznspUgPfEHSOXltQGSPQ2YS+yxaYVgMBl1TgQuXW1\n" +
            "VB3cAsdRFJN6yG15Pn4sZuh3hXMrnToIJXYBwgq1AaYms7RBY5ULC8m+oO5sE9az\n" +
            "jIaIeaJZEfEEIJ1MpZFMXTZKRPnVRoSTH/y6JdOKwN+ydyrmMcTBdSZezRlyn8Oc\n" +
            "OgOizCp2H0ZB5L0xydXiphobu2c3gD19BSqGqX6/2PJU6fulpGAoPhUUvV2JOLYj\n" +
            "SG9BJSPnAZUu6e6xwFdgSet6QucYpCmSxtaJ0j5oRvm+LYwOvWVXJMgHgjkcqAan\n" +
            "nggfynIsy8Adm/bPJy6wlJYEmMo26NU7J9nViJ3P9+KG4qBN0jmxjtcVZP0kT+nE\n" +
            "+1Lr8jnToyNoHpxBvE+WFeMsTImkiWvq4lcLfOPjsSEPcsRtM2kCggEBAPvXIXsY\n" +
            "uwGl9hCxPKRk5li+rOGx67AiTceHC85LNMOns0pd+gZFiT68a7CUumA6dK7j7sFV\n" +
            "1gARE60bu7tZd9DtHIseRyLg4Ib7036kLTlRLsYxtJcV2loCyUL8Ns3wCyie+pOd\n" +
            "deVG2Kuxigtm6/C7S6LrtDNPzI0pz2W4ZomBeGvI6YI9Z04Xs/LS3eSjY6yvFS0Y\n" +
            "WUqt0nQoXRreMntIifElbH2g87t6AgpCmvblxLJL7t17v5poZR1zC5NAK6jUAVMk\n" +
            "NO5gU1nVqcQhB1BZYd25212gJ45tFK0Y3Tv0/sacoqIQpx3p2foXRSPPcuOclQbu\n" +
            "GQWQepTzQKpztOUCggEBAPE5BhLLtEdlkRs8DJR3IuWFjA7EeWDcQx/R7UnGZREP\n" +
            "gj1K2Auk0gcz459qc6USiHgmhEtBrl0ElpPJ9rupmGAor3f9WATWxGUxCy904mFk\n" +
            "EfS4xKDX4BU/SD74ntWU66CDYY7ME6z0ImWOI+4NDezRybQDIqQB/qgmhegiQU9S\n" +
            "tfM5MDGkeA+cnNCOQA5dj/OuteWvfuRHRLqiNhnCfY5qyXkjGHIpiGAMeoCv/xhT\n" +
            "PG1VFeDM11pT3g/Nrww1CrFGfn1NbeWYgN9sogpI2GRySpeLEEkz8/q4UXJikXLA\n" +
            "jSjk6+3IGhtEtBh67xpRh44vaDMi0CgOkGzuj57qt2MCggEBAM0SE0tfiff/om1Z\n" +
            "RZ5AI2okajamYNteM6PEDhMAHokr580o1U2CFg4l9DMoCNQPBbDcHjEEqmwHe9Q8\n" +
            "fxRB+FUCn3mRQC/gmjSQn1nCaqj/uGzfUMSuoM0m86g+JIpxa0S6oCTP1Y7Lr+ev\n" +
            "fE9okbvHaAHUmTonQtO4NhAIpkZWTS7HvY22Xoayum+C0fk6KxIUx9OevicXgAXi\n" +
            "UF5ujQDiwqDA7qUmCRKieULNlc5yr0bYmjt0Z0NeuufOJKDVYjfp2uJO22M4wOao\n" +
            "G8N/east/YlFrKGCpnNN08ZYNt5503T7W1tI4Cx1nnANvtk4Nb+uY5PvEiT7zzW3\n" +
            "FvM1oRkCggEARUKLNOBaLdnCtSAjuIkQcvhKQ380HpR/jWZYDEOzhxd93J1dtNIp\n" +
            "H7pNU9tbwvy0RFNCztzX9Is5tMtbVXyXbJyh7QDHwfPsReW0AXc8/Juf8DUQ9+Gj\n" +
            "zFX0J2h5Knfsw5SVcUu1JoaeTaxnMpV69xExKcdwrciI1rzg28riF6V26pPz+3zu\n" +
            "oATmnZcKW1WP3HbAcWdXNrIj0LZ8dB78r+SHXomuHjFAn9tVXRerVc+oXPf+JSKL\n" +
            "frrv2nfDF+0u2cGU1eNid3a3Ie9uAyfxQb3zHtnl3i7VkZpUgFQ+0MTqguBfwVEf\n" +
            "9c5fK1w4xMS9+BwyfeQ0AhxUaYKBqC5aYwKCAQB6rNenMjL5tfA+EENz1F8UV0zp\n" +
            "uQfx558vU+zzrxsnOW7v0ZdIxYAuv8bYC80e30zPNHZSV/W31ff7HR3YrXPBVAOP\n" +
            "oh6lnay05E6wCYICW67trybOD12yd7bQchYn8tpXAwRQ+0OZkvjox4u8dPucoRgd\n" +
            "zR9Td5q0K4P46ccFOQP3J6R+/PwK0dEbMzfvy/2az45W4+sdOMr+/EIA4qQNTDZ6\n" +
            "bFnlpBWLX5FI5NgPV1pmxJw9qiBUItfk+Z3xPnC6cr47u1MtFF6hQfX2RmxHUV/M\n" +
            "ycs2vQxOSITuOTZmUzwvZgk4xi4F/Cv4vTnto2dB1HqwiHAeqiu9lGbn8Zio\n" +
            "-----END RSA PRIVATE KEY-----\n";
    private Boolean needOauth2 = false;
    private final String kongConfigName = "ConfigKong";
    private final String PLUGIN_NAME = "jwt";

    private Api api;
    private Consumer consumer;
    private Plugin plugin;
    private JwtCredential credential;

    public KongJwtServiceImpl() {
        initConfigs();
        kongClient = new KongClient(KONG_ADMIN_URL, KONG_AUTH2_API_URL, needOauth2);
    }

    private void initConfigs() {
        KONG_ADMIN_URL = (String) ConfigUtil.getConfigProp("KONG_ADMIN_URL", kongConfigName);
        KONG_PROXY_URL = (String) ConfigUtil.getConfigProp("KONG_PROXY_URL", kongConfigName);
        KONG_AUTH2_API_URL = (String) ConfigUtil.getConfigProp("KONG_AUTH2_API_URL", kongConfigName);
        API_UPSTREAM_URL = (String) ConfigUtil.getConfigProp("API_UPSTREAM_URL", kongConfigName);
        API_NAME = (String) ConfigUtil.getConfigProp("API_NAME", kongConfigName);
        API_URIS = (String) ConfigUtil.getConfigProp("API_URIS", kongConfigName);
        API_METHODS = (String) ConfigUtil.getConfigProp("API_METHODS", kongConfigName);
        CONSUMER_NAME = (String) ConfigUtil.getConfigProp("CONSUMER_NAME", kongConfigName);
        if (KONG_AUTH2_API_URL != null && !KONG_AUTH2_API_URL.isEmpty()) {
            needOauth2 = true;
        }
    }

    private Api getKongAPIByName() {
        try {
            return kongClient.getApiService().getApi(API_NAME);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    private Consumer getKongConsumerByName() {
        try {
            return kongClient.getConsumerService().getConsumer(CONSUMER_NAME);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


    private Plugin getJwtPlugin() {
        try {
            List<Plugin> plugins = new ArrayList<>();
            PluginList pluginList = kongClient.getPluginService().listPlugins(null, null, null, null, 1L, null);
            plugins.addAll(pluginList.getData());
            while (pluginList.getOffset() != null) {
                pluginList = kongClient.getPluginService().listPlugins(null, null, null, null, 1L, pluginList.getOffset());
                plugins.addAll(pluginList.getData());
            }
            for (Plugin plugin : plugins) {
                if (PLUGIN_NAME.equals(plugin.getName())) {
                    return plugin;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public JsonStatus createAPIGateWayHS256() {
        return createAPIGateWay("HS256");
    }

    @Override
    public JsonStatus createAPIGateWayRS256() {
        return createAPIGateWay("RS256");
    }

    private JsonStatus createAPIGateWay(String algorithm) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            api = getKongAPIByName();
            if (api == null) {
                api = createKongAPI();
            }
            consumer = getKongConsumerByName();
            if (consumer == null) {
                consumer = createKongConsumer();
            }
            plugin = getJwtPlugin();
            if (plugin == null) {
                plugin = createJwtPlugin(algorithm);
            }
            credential = getJwtCredential(algorithm);
            if (credential == null) {
                if ("RS256".equals(algorithm)) {
                    credential = createJwtCredentialRS256();
                } else {
                    credential = createJwtCredentialHS256();
                }
            }
            jsonStatus.setSuccess(true);
            jsonStatus.setMsg("create success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("create failed");
        }
        return jsonStatus;
    }

    @Override
    public JsonStatus clearAPIGateWayHS256() {
        return clearAPIGateWay("HS256");
    }

    @Override
    public JsonStatus clearAPIGateWayRS256() {
        return clearAPIGateWay("RS256");
    }

    private JsonStatus clearAPIGateWay(String algorithm) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            consumer = getKongConsumerByName();
            credential = getJwtCredential(algorithm);
            if (consumer != null && credential != null) {
                deleteJwtCredential();
            }
            plugin = getJwtPlugin();
            if (plugin != null) {
                deleteJwtPlugin();
            }
            consumer = getKongConsumerByName();
            if (consumer != null) {
                deleteKongConsumer();
            }
            api = getKongAPIByName();
            if (api != null) {
                deleteKongAPI();
            }
            jsonStatus.setSuccess(true);
            jsonStatus.setMsg("delete success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("delete failed");
        }
        return jsonStatus;
    }

    private void deleteJwtCredential() {
        kongClient.getJwtService().deleteCredentials(consumer.getId(), credential.getId());
    }

    private void deleteJwtPlugin() {
        kongClient.getPluginService().deletePlugin(plugin.getId());
    }

    private void deleteKongConsumer() {
        kongClient.getConsumerService().deleteConsumer(consumer.getId());
    }

    private void deleteKongAPI() {
        kongClient.getApiService().deleteApi(api.getId());
    }

    private JwtCredential createJwtCredentialHS256() {
        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential());
    }

    private JwtCredential createJwtCredentialRS256() {
        String pubkey1 = PUBLIC_KEY_START + PUBLIC_KEY + PUBLIC_KEY_END;
//        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential(pubkey, "RS256"));
        String pubKey = getPublicKey();
        System.out.println("pubkey equal:");
        System.out.println(pubkey1 == pubKey);
        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential(pubKey, "RS256"));
    }

    private JwtCredential getJwtCredential(String algorithm) {
        try {
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            List<JwtCredential> credentials = list.getData();
            if (credentials != null && credentials.size() > 0) {
                for (JwtCredential jwtCredential : credentials) {
                    if (algorithm.equals(jwtCredential.getAlgorithm())) {
                        return jwtCredential;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Plugin createJwtPlugin(String algorithm) {
        Plugin plugin = new Plugin();
        plugin.setApiId(api.getId());
        plugin.setName(PLUGIN_NAME);
        plugin.setEnabled(true);
        plugin.setCreatedAt(System.currentTimeMillis());
        JwtConfig config = new JwtConfig();
        if ("RS256".equals(algorithm)) {
            config.setSecretIsBase64(false);
        } else {
            config.setSecretIsBase64(true);
        }
        plugin.setConfig(config);
        return kongClient.getPluginService().addPlugin(plugin);
    }

    private Consumer createKongConsumer() {
        Consumer consumer = new Consumer();
        consumer.setCustomId(UUID.randomUUID().toString());
        consumer.setUsername(CONSUMER_NAME);
        return kongClient.getConsumerService().createConsumer(consumer);
    }

    private Api createKongAPI() {
        Api request = new Api();
        request.setUris(Arrays.asList(API_URIS.split(",")));
        request.setName(API_NAME);
        request.setUpstreamUrl(API_UPSTREAM_URL);
        request.setMethods(Arrays.asList(API_METHODS.split(",")));
        return kongClient.getApiService().createApi(request);
    }

    private Api updateKongAPI(Api api) {
        Api request = new Api();
        request.setUris(api.getUris());
        request.setName(api.getName());
        request.setUpstreamUrl(api.getUpstreamUrl());
        request.setMethods(api.getMethods());
        return kongClient.getApiService().updateApi(api.getName(), request);
    }

    @Override
    public String getJwtTokenHS256() {
        try {
            Consumer consumer = getKongConsumerByName();
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            List<JwtCredential> jwtCredentials = list.getData();
            if (jwtCredentials != null && !jwtCredentials.isEmpty()) {
                for (JwtCredential jwtCredential : jwtCredentials) {
                    if ("HS256".equals(jwtCredential.getAlgorithm())) {
                        return generateJwtStringHS256(jwtCredential);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getJwtTokenRS256() {
        try {
            Consumer consumer = getKongConsumerByName();
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            List<JwtCredential> jwtCredentials = list.getData();
            if (jwtCredentials != null && !jwtCredentials.isEmpty()) {
                for (JwtCredential jwtCredential : jwtCredentials) {
                    if ("RS256".equals(jwtCredential.getAlgorithm())) {
//                        return generateJwtStringRS256(jwtCredential, PRIVATE_KEY);
                        String privateKey = getPrivateKey();
                        System.out.println("privatekey equal:");
                        System.out.println(PRIVATE_KEY == privateKey);
                        return generateJwtStringRS256(jwtCredential, privateKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPrivateKey() {
        String privateKey = this.jwtService.getPrivateKeyString();
        if (privateKey != null) {
            privateKey = privateKey.replaceAll("\r", "");
            privateKey = privateKey.trim();
        }
        return privateKey;
    }

    private String getPublicKey() {
        String pubKey = this.jwtService.getPublicKeyString();
        if (pubKey != null) {
            pubKey = pubKey.replaceAll("\r", "");
            pubKey = pubKey.trim();
        }
        return pubKey;
    }

    private String generateJwtStringHS256(JwtCredential jwtCredential) {
        // 签名加密base64
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtCredential.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("iss", jwtCredential.getKey())
                .claim("name", CONSUMER_NAME)
                .claim("iat", jwtCredential.getCreatedAt())
                .signWith(SignatureAlgorithm.HS256, signingKey);
        return builder.compact();
    }

    private String generateJwtStringRS256(JwtCredential jwtCredential, String privateKey) {
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("typ", "JWT")
                .claim("iss", jwtCredential.getKey())
                .claim("name", CONSUMER_NAME)
                .claim("iat", jwtCredential.getCreatedAt())
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(privateKey));
        return builder.compact();
    }

    public Api getApi() {
        return api;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * 获取PrivateKey对象
     * @param privateKeyBase64
     * @return
     */
    private PrivateKey getPrivateKey(String privateKeyBase64) {
//        String privKeyPEM = privateKeyBase64
//                .replaceAll("\\-*BEGIN.*KEY\\-*", "")
//                .replaceAll("\\-*END.*KEY\\-*", "");
        String privKeyPEM = privateKeyBase64.replaceAll("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll("\n", "");
        // Base64 decode the data
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

    public void setJwtService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }
}
