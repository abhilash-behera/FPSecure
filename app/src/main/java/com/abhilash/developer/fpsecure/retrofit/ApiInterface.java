package com.abhilash.developer.fpsecure.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Abhilash on 17-09-2017
 */

public interface ApiInterface {

    @Headers("Content-type: application/json")
    @POST("/signUp")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    @Headers("Content-type: application/json")
    @POST("/updateToken")
    Call<UpdateTokenResponse> updateToken(@Body UpdateTokenRequest updateTokenRequest);

    @Headers("Content-type:application/json")
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-type:application/json")
    @POST("/checkUserExistence")
    Call<UserExistenceResponse> checkUserExistence(@Body UserExistenceRequest userExistenceRequest);

    @Multipart
    @POST("/sendDocument")
    Call<SendDocumentResponse> sendDocument(
            @Part("description")RequestBody description,
            @Part("to")RequestBody to,
            @Part("from")RequestBody from,
            @Part MultipartBody.Part file);

    @Headers("Content-type:application/json")
    @POST("/sentDocuments")
    Call<SentDocumentsResponse> getSentDocuments(@Body SentDocumentsRequest sentDocumentsRequest);

    @Headers("Content-type:application/json")
    @POST("/receivedDocuments")
    Call<ReceivedDocumentsResponse> getReceivedDocuments(@Body ReceivedDocumentsRequest receivedDocumentsRequest);

    @Headers("Content-type:application/json")
    @POST("/approveDocument")
    Call<ApproveDocumentResponse> approveDocument(@Body ApproveDocumentRequest approveDocumentRequest);

    @Headers("Content-type:application/json")
    @POST("/rejectDocument")
    Call<RejectDocumentResponse> rejectDocument(@Body RejectDocumentRequest rejectDocumentRequest);
}
