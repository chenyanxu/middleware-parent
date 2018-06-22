package com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication;

import com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication.HmacAuthService;
import com.kalix.middleware.kongclient.biz.kong.exception.KongClientException;
import com.kalix.middleware.kongclient.biz.kong.internal.plugin.authentication.RetrofitHmacAuthService;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.hmac.HmacAuthCredential;
// import retrofit2.Retrofit;
// import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Created by vaibhav on 15/06/17.
 *
 * Updated by fanhua on 2017-08-07.
 */
public class HmacAuthServiceImpl implements HmacAuthService {

    private RetrofitHmacAuthService retrofitHmacAuthService;

    public HmacAuthServiceImpl(RetrofitHmacAuthService retrofitHmacAuthService) {
        this.retrofitHmacAuthService = retrofitHmacAuthService;
    }

    @Override
    public void addCredentials(String consumerIdOrUsername, String username, String secret) {
        try {
            retrofitHmacAuthService.addCredentials(consumerIdOrUsername, new HmacAuthCredential(username, secret)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
