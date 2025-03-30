package com.example.cursored.api;

import com.example.cursored.model.ModelResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarApiService {
    @GET("api/0.3/")
    Call<ModelResponse> getModels(
        @Query("cmd") String cmd,
        @Query("make") String make,
        @Query("year") int year,
        @Query("sold_in_us") int soldInUs
    );

    @GET("api/0.3/")
    Call<ModelResponse> getMakes(
        @Query("callback") String callback,
        @Query("cmd") String cmd
    );

    @GET("api/0.3/")
    Call<ModelResponse> getCarSpecs(
        @Query("callback") String callback,
        @Query("cmd") String cmd,
        @Query("make") String make,
        @Query("model") String model,
        @Query("year") int year
    );

    @GET("api/0.3/")
    Call<ModelResponse> getCarImage(
        @Query("callback") String callback,
        @Query("cmd") String cmd,
        @Query("make") String make,
        @Query("model") String model,
        @Query("year") int year
    );
} 