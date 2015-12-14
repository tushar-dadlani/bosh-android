package io.pivotal.bosh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;

import io.pivotal.bosh.response.DeploymentManifest;

public class DeploymentManifestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment_manifest);

        Intent intent = getIntent();

        final HashMap<String,String> jobs = (HashMap<String,String>) intent.getSerializableExtra("jobs");

        final DeploymentManifest deploymentManifest = (DeploymentManifest) intent.getSerializableExtra("deployment_manifest");
        final ListView jobListView = (ListView) findViewById(R.id.job_list);

        HashMapAdapter hashMapAdapter = new HashMapAdapter(DeploymentManifestActivity.this, jobs);

        jobListView.setAdapter(hashMapAdapter);


        final Button deployButton = (Button)findViewById(R.id.deploy_button);
        deployButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newManifest = deploymentManifest.updateJobs(jobs);
                



            }
        });



    }

}
