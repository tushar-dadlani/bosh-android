package io.pivotal.bosh;

import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by pivotal on 12/13/15.
 */
public class DeploymentManifestParserTest extends TestCase {


    public void testConstructor() {
        DeploymentManifestParser parser = new DeploymentManifestParser("---");
        assertSame(Map.class, parser.manifestMap);
    }


}