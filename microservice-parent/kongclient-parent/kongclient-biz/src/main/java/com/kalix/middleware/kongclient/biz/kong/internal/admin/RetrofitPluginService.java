package com.kalix.middleware.kongclient.biz.kong.internal.admin;


import com.kalix.middleware.kongclient.biz.kong.model.admin.plugin.Plugin;
import com.kalix.middleware.kongclient.biz.kong.model.admin.plugin.PluginList;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by vaibhav on 12/06/17.
 *
 * Updated by fanhua on 2017-08-05.
 */
public interface RetrofitPluginService {

    @POST("plugins/")
    Call<Plugin> addPlugin(@Body Plugin request);

    @GET("plugins/{id}")
    Call<Plugin> getPlugin(@Path("id") String nameOrId);

    @PATCH("plugins/{id}")
    Call<Plugin> updatePlugin(@Path("id") String nameOrId, @Body Plugin request);

    @PUT("plugins/")
    Call<Plugin> createOrUpdatePlugin(@Body Plugin request);

    @DELETE("plugins/{id}")
    Call<Void> deletePlugin(@Path("id") String nameOrId);

    @GET("plugins/")
    Call<PluginList> listPlugins(@Query("id") String id, @Query("api_id") String apiId, @Query("consumer_id") String consumerId, @Query("name") String name, @Query("size") Long size, @Query("offset") String offset);


}
