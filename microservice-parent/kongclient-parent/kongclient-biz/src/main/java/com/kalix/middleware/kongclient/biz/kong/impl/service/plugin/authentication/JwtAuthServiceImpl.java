package com.kalix.middleware.kongclient.biz.kong.impl.service.plugin.authentication;

import com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication.JwtService;
import com.kalix.middleware.kongclient.biz.kong.exception.KongClientException;
import com.kalix.middleware.kongclient.biz.kong.internal.plugin.authentication.RetrofitJwtService;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredential;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredentialList;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by dvilela on 10/16/17.
 */
public class JwtAuthServiceImpl implements JwtService {

    private RetrofitJwtService retrofitJwtService;

    public JwtAuthServiceImpl(RetrofitJwtService retrofitJwtService) {
        this.retrofitJwtService = retrofitJwtService;
    }

    @Override
    public JwtCredential addCredentials(String consumerIdOrUsername, JwtCredential request) {
        try {
            Call<JwtCredential> call = retrofitJwtService.addCredentials(consumerIdOrUsername, request);
            Response<JwtCredential> response =  call.execute();
            JwtCredential jc = response.body();
//            return retrofitJwtService.addCredentials(consumerIdOrUsername, request).execute().body();
            return jc;
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public void deleteCredentials(String consumerIdOrUsername, String id) {
        try {
            retrofitJwtService.deleteCredentials(consumerIdOrUsername, id).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public JwtCredentialList listCredentials(String consumerIdOrUsername, Long size, String offset) {
        try {
            return retrofitJwtService.listCredentials(consumerIdOrUsername, size, offset).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
