package io.pivotal.bosh.response;

import android.util.Log;

import org.yaml.snakeyaml.Yaml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by pivotal on 12/12/15.
 */
public class DeploymentManifest implements Serializable{
    public String manifest;


    public String updateJobs(HashMap<String, String> jobs) {
        Yaml yaml = new Yaml();
        Map<String, Object> deploymentManifest = (Map<String, Object>) yaml.load(manifest);



        ArrayList<Object> manifestJobs;
        manifestJobs = (ArrayList<Object>) deploymentManifest.get("jobs");
        ListIterator<Object> jobIterator =  manifestJobs.listIterator();

        ArrayList<Object> newManifestJobs = new ArrayList<Object>();
        while(jobIterator.hasNext()) {

            HashMap<String, String> jobObject;
            jobObject = (HashMap<String, String>)  jobIterator.next();
            jobObject.put("instances", jobs.get(jobObject.get("name")));

            Log.d("JobObject", jobObject.toString());

            newManifestJobs.add(jobObject);
        }

        deploymentManifest.put("jobs", newManifestJobs);

        String dumpedYaml = yaml.dump(deploymentManifest);
        Log.d("NewManifest",dumpedYaml);

        return dumpedYaml;

    }
}
