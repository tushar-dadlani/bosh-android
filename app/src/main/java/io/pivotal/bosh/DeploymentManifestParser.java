package io.pivotal.bosh;

import android.util.Log;

import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by pivotal on 12/13/15.
 */
public class DeploymentManifestParser {


    Map<String, Object> manifestMap;

    public DeploymentManifestParser(String yamlString) {
        Yaml yaml = new Yaml();
        manifestMap = (Map<String, Object>) yaml.load(yamlString);
        Log.d("ManifestParser", manifestMap.toString());
    }

    public HashMap<String, Integer> getJobInstances() {

        ArrayList<Object> jobs = (ArrayList<Object>) manifestMap.get("jobs");
        ListIterator<Object> jobIterator =  jobs.listIterator();
        HashMap<String, Integer> jobInstances = new HashMap<>();
        while(jobIterator.hasNext()) {
            Object job = jobIterator.next();
            Log.d("jobObj", job.toString());
            Map<String, Object> jobMap = (Map<String, Object>) job;

            String jobName = (String)jobMap.get("name");
            Integer instances = (Integer)jobMap.get("instances");

            jobInstances.put(jobName, instances);

        }
        Log.d("JobInstances", jobInstances.toString());
        return jobInstances;

    }
}
