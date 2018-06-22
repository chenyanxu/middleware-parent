package com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication;

import com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication.BasicAuthService;
import com.kalix.middleware.kongclient.biz.kong.exception.KongClientException;
import com.kalix.middleware.kongclient.biz.kong.internal.plugin.authentication.RetrofitBasicAuthService;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.basic.BasicAuthCredential;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Created by vaibhav on 15/06/17.
 *
 * Updated by fanhua on 2017-08-07.
 */
public class BasicAuthServiceImpl implements BasicAuthService {

    private RetrofitBasicAuthService retrofitBasicAuthService;

    public BasicAuthServiceImpl(RetrofitBasicAuthService retrofitBasicAuthService) {
        this.retrofitBasicAuthService = retrofitBasicAuthService;
    }

    @Override
    public void addCredentials(String consumerIdOrUsername, String username, String password) {
        try {
            retrofitBasicAuthService.addCredentials(consumerIdOrUsername, new BasicAuthCredential(username, password)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
