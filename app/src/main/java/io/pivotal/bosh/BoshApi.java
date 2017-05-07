package io.pivotal.bosh;

// Use otto to handle message bus based communication between UI and

import com.squareup.okhttp.RequestBody;

import java.util.List;

import io.pivotal.bosh.response.BoshDeployment;
import io.pivotal.bosh.response.BoshInfo;
import io.pivotal.bosh.response.BoshStemcell;
import io.pivotal.bosh.response.DeploymentManifest;
import io.pivotal.bosh.response.Task;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BoshApi {
    //private String ENDPOINT = "https://:25555/info";


    @GET("/info")
    Call<BoshInfo> getBoshDetails();

    @GET("/stemcells")
    Call<List<BoshStemcell>> getStemcells();

    @GET("/deployments")
    Call<List<BoshDeployment>> getDeployments();

    @GET("/deployments/{name}")
    Call<DeploymentManifest> getDeploymentManifest(@Path("name") String deployment_name);


    @POST("/deployments")
    @Headers("Content-Type:text/yaml")
    Call<Task> deployManifest(@Body RequestBody deploymentManifest);


}
