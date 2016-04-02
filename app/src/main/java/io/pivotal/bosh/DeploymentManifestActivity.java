package io.pivotal.bosh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;

import io.pivotal.bosh.response.DeploymentManifest;
import io.pivotal.bosh.response.Task;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DeploymentManifestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment_manifest);

        Retrofit retrofit = new Retrofit.Builder()
                .client(HttpHelpers.getUnsafeOkHttpClient())
                .baseUrl("https://bosh.walnut.cf-app.com:25555")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final BoshApi boshApi = retrofit.create(BoshApi.class);

        Intent intent = getIntent();

        final HashMap<String,Integer> jobs = (HashMap<String,Integer>) intent.getSerializableExtra("jobs");

        final DeploymentManifest deploymentManifest = (DeploymentManifest) intent.getSerializableExtra("deployment_manifest");
        final ListView jobListView = (ListView) findViewById(R.id.job_list);

        HashMapAdapter hashMapAdapter = new HashMapAdapter(DeploymentManifestActivity.this, jobs);

        jobListView.setAdapter(hashMapAdapter);


        final Button deployButton = (Button)findViewById(R.id.deploy_button);
        deployButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newManifest = deploymentManifest.updateJobs(jobs);
                Log.d("Deployment", newManifest);

    Call<Task> deployCall = boshApi.deployManifest(RequestBody.create(MediaType.parse("ALL"),newManifest));
    deployCall.enqueue(new Callback<Task>() {
        @Override
        public void onResponse(Response<Task> response, Retrofit retrofit) {

            Log.d("TaskDescription", response.body().description);

        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    });




            }
        });



    }

}
