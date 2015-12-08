package io.pivotal.bosh;

// Use otto to handle message bus based communication between UI and

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

public interface BoshApi {
    //private String ENDPOINT = "https://:25555/info";


    @GET("/info")
    Call<BoshInfo> getBoshDetails();

    @GET("/stemcells")
    Call<List<BoshStemcell>> getStemcells();

}
