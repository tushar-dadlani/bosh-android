package io.pivotal.bosh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import io.pivotal.bosh.response.BoshDeployment;
import io.pivotal.bosh.response.DeploymentManifest;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DeploymentListActivity extends FragmentActivity {


    private TextView boshInfo;
    private Context ctx;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deployment_list_view);

        Retrofit retrofit = new Retrofit.Builder()
                .client(HttpHelpers.getUnsafeOkHttpClient())
                .baseUrl("https://bosh.walnut.cf-app.com:25555")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final BoshApi boshApi = retrofit.create(BoshApi.class);

         ctx = getApplicationContext();


        Call<List<BoshDeployment>> callDeployments = boshApi.getDeployments();
        callDeployments.enqueue(new Callback<List<BoshDeployment>>() {
            @Override
            public void onResponse(Response<List<BoshDeployment>> response, Retrofit retrofit) {
                Log.d("Response: ", response.message());
                ListIterator<BoshDeployment> deploymentListIterator = response.body().listIterator();
                ListView deployments = (ListView) findViewById(R.id.deployment_list);


                final ArrayList<String> boshDeployments = new ArrayList<String>();


                while (deploymentListIterator.hasNext()) {
                    BoshDeployment boshDeployment = deploymentListIterator.next();
                    boshDeployments.add(boshDeployment.name);
                }

                ArrayAdapter<String> deploymentAdapter = new ArrayAdapter<String>(DeploymentListActivity.this, R.layout.bosh_deployment, boshDeployments);

                deployments.setAdapter(deploymentAdapter);
                deployments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String deploymentName = boshDeployments.get(position);
                        Log.d("Deployment", deploymentName);

                        Call<DeploymentManifest> getDeploymentManifest = boshApi.getDeploymentManifest(deploymentName);
                        getDeploymentManifest.enqueue(new Callback<DeploymentManifest>() {
                                                          @Override
                                                          public void onResponse(Response<DeploymentManifest> response, Retrofit retrofit) {
                                                              Log.i("ManifestMsg", response.message());

                                                              DeploymentManifest deploymentManifest = response.body();
                                                              Log.i("ManifestBody", deploymentManifest.manifest);
                                                              DeploymentManifestParser dmf = new DeploymentManifestParser(response.body().manifest);
                                                              HashMap<String, Integer> jobs = dmf.getJobInstances();
                                                              Log.d("JobInstances", jobs.toString());

                                                              Intent intent = new Intent(DeploymentListActivity.this, DeploymentManifestActivity.class);
                                                              intent.putExtra("jobs", jobs);
                                                              intent.putExtra("deployment_manifest", deploymentManifest);
                                                              startActivity(intent);
//                                for (Map.Entry<String, String> entry : jobs.entrySet()) {
//
//                            }


                                                          }

                                                          @Override
                                                          public void onFailure(Throwable t) {
                                                              Helpers.longToast(ctx,"Cannot get the jobs for the selected manifest");
                                                              t.printStackTrace();

                                                          }
                                                      }

                        );


                    }
                });
            }


            @Override
            public void onFailure(Throwable t) {
                Helpers.longToast(ctx,"Cannot reach BOSH director, check your network connection");
                t.printStackTrace();

            }
        });


    }


}