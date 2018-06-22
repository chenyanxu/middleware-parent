package com.kalix.middleware.kongclient.biz.kong.internal.plugin.authentication;

import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.basic.BasicAuthCredential;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vaibhav on 15/06/17.
 */
public interface RetrofitBasicAuthService {

    @POST("consumers/{id}/basic-auth")
    Call<BasicAuthCredential> addCredentials(@Path("id") String consumerIdOrUsername, @Body BasicAuthCredential request);
}
