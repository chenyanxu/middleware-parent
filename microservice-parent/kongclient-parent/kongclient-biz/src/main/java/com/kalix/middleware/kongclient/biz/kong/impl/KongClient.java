package com.kalix.middleware.kongclient.biz.kong.impl;

import com.kalix.middleware.kongclient.biz.kong.api.admin.*;
import com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication.*;
import com.kalix.middleware.kongclient.biz.kong.api.plugin.security.AclService;
import com.kalix.middleware.kongclient.biz.kong.impl.helper.RetrofitServiceCreator;
import com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication.BasicAuthServiceImpl;
import com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication.HmacAuthServiceImpl;
import com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication.JwtAuthServiceImpl;
import com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication.KeyAuthServiceImpl;
import com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.security.AclServiceImpl;
import com.kalix.middleware.kongclient.biz.kong.internal.admin.*;
import com.kalix.middleware.kongclient.biz.kong.internal.plugin.authentication.*;
import com.kalix.middleware.kongclient.biz.kong.internal.plugin.security.RetrofitAclService;
//import lombok.Data;

/**
 * Created by vaibhav on 12/06/17.
 *
 * Updated by fanhua on 2017-08-07.
 *
 * Updated by dvilela on 17/10/17.
 */
//@Data
public class KongClient {

    private ConsumerService consumerService;

    private ApiService apiService;
    private ApiPluginService apiPluginService;

    private PluginService pluginService;
    private PluginRepoService pluginRepoService;
    private CertificateService certificateService;
    private SniService sniService;
    private UpstreamService upstreamService;
    private TargetService targetService;

    private BasicAuthService basicAuthService;
    private KeyAuthService keyAuthService;
    private HmacAuthService hmacAuthService;
    private JwtService jwtService;

    private OAuth2ProcessService oAuth2ProcessService;
    private OAuth2ManageService oAuth2ManageService;

    private AclService aclService;


    public KongClient(String adminUrl) {
        this(adminUrl, null, false);
    }


    public KongClient(String adminUrl, String proxyUrl, boolean needOAuth2Support) {

        if (adminUrl == null || adminUrl.isEmpty()) {
            throw new IllegalArgumentException("The adminUrl cannot be null or empty!");
        }

        if (needOAuth2Support) {
            if (proxyUrl == null || proxyUrl.isEmpty()) {
                throw new IllegalArgumentException("The proxyUrl cannot be null or empty!");
            }
            if (!proxyUrl.startsWith("https://")) {
                throw new IllegalArgumentException("The proxyUrl must use https if you need OAuth2 support!");
            }
        }


        RetrofitServiceCreator retrofitServiceCreatorForAdminUrl = new RetrofitServiceCreator(adminUrl);

        {
            consumerService = retrofitServiceCreatorForAdminUrl.create(ConsumerService.class, RetrofitConsumerService.class);

            apiService = retrofitServiceCreatorForAdminUrl.create(ApiService.class, RetrofitApiService.class);
            apiPluginService = retrofitServiceCreatorForAdminUrl.create(ApiPluginService.class, RetrofitApiPluginService.class);

            pluginService = retrofitServiceCreatorForAdminUrl.create(PluginService.class, RetrofitPluginService.class);
            pluginRepoService = retrofitServiceCreatorForAdminUrl.create(PluginRepoService.class, RetrofitPluginRepoService.class);

            certificateService = retrofitServiceCreatorForAdminUrl.create(CertificateService.class, RetrofitCertificateService.class);
            sniService = retrofitServiceCreatorForAdminUrl.create(SniService.class, RetrofitSniService.class);
            upstreamService = retrofitServiceCreatorForAdminUrl.create(UpstreamService.class, RetrofitUpstreamService.class);
            targetService = retrofitServiceCreatorForAdminUrl.create(TargetService.class, RetrofitTargetService.class);
        }

        {
            basicAuthService = new BasicAuthServiceImpl(retrofitServiceCreatorForAdminUrl.createRetrofitService(RetrofitBasicAuthService.class));
            keyAuthService = new KeyAuthServiceImpl(retrofitServiceCreatorForAdminUrl.createRetrofitService(RetrofitKeyAuthService.class));
            hmacAuthService = new HmacAuthServiceImpl(retrofitServiceCreatorForAdminUrl.createRetrofitService(RetrofitHmacAuthService.class));
            jwtService = new JwtAuthServiceImpl(retrofitServiceCreatorForAdminUrl.createRetrofitService(RetrofitJwtService.class));
            aclService = new AclServiceImpl(retrofitServiceCreatorForAdminUrl.createRetrofitService(RetrofitAclService.class));
        }

        if(needOAuth2Support) {

            RetrofitServiceCreator retrofitServiceCreatorForProxyUrl = new RetrofitServiceCreator(proxyUrl);

            //oauth2 process is on proxy port
            oAuth2ProcessService = retrofitServiceCreatorForProxyUrl.create(OAuth2ProcessService.class, RetrofitOAuth2ProcessService.class);

            //oauth2 manage is on admin port
            oAuth2ManageService = retrofitServiceCreatorForAdminUrl.create(OAuth2ManageService.class, RetrofitOAuth2ManageService.class);
        }

    }

    public ConsumerService getConsumerService() {
        return consumerService;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public ApiPluginService getApiPluginService() {
        return apiPluginService;
    }

    public PluginService getPluginService() {
        return pluginService;
    }

    public PluginRepoService getPluginRepoService() {
        return pluginRepoService;
    }

    public CertificateService getCertificateService() {
        return certificateService;
    }

    public SniService getSniService() {
        return sniService;
    }

    public UpstreamService getUpstreamService() {
        return upstreamService;
    }

    public TargetService getTargetService() {
        return targetService;
    }

    public BasicAuthService getBasicAuthService() {
        return basicAuthService;
    }

    public KeyAuthService getKeyAuthService() {
        return keyAuthService;
    }

    public HmacAuthService getHmacAuthService() {
        return hmacAuthService;
    }

    public JwtService getJwtService() {
        return jwtService;
    }

    public OAuth2ProcessService getOAuth2ProcessService() {
        return oAuth2ProcessService;
    }

    public OAuth2ManageService getOAuth2ManageService() {
        return oAuth2ManageService;
    }

    public AclService getAclService() {
        return aclService;
    }
}
