package com.example.feng.xgs.core.net;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RestService {


    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @GET
    Call<String> getRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);
//    @FormUrlEncoded
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @POST
    Call<String> postFile(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

//    @FormUrlEncoded
    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * Streaming 边下载一边写入，避免一次性在内存中写入过大的文件，造成app内存溢出
     * */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> uploadParams(@Url String url, @Part MultipartBody.Part raw, @Part MultipartBody.Part file);

//    @Multipart
//    @POST()
//    Call<String> upload(@Url String url, @Part("image") TypedByteArray input);

    @FormUrlEncoded
    @POST
    Call<String> postsms(@Url String url, @FieldMap Map<String, Object> params);

    @Multipart
    @POST
    Call<String> uploadMore(@Url String url, @PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST
    Call<String> uploadWithParams(@Url String url, @PartMap Map<String, RequestBody> map);

}
