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

    public HashMap<String, String> getJobInstances() {

        ArrayList<Object> jobs = (ArrayList<Object>) manifestMap.get("jobs");
        ListIterator<Object> jobIterator =  jobs.listIterator();
        HashMap<String, String> jobInstance = new HashMap<>();
        while(jobIterator.hasNext()) {
            Object job = jobIterator.next();
            HashMap<String, String> jobHash = (HashMap<String,String>)job;

            jobInstance.put(jobHash.get("name"), jobHash.get("instances"));
        }

        return jobInstance;

    }
}
