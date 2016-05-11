package org.daven.infinispan;

import org.daven.shrinkwrap.DeployableUnit;
import org.junit.Test;

public class DeployableTest {

    DeployableUnit deployableUnit = DeployableUnit.getOne();

    String deploymentPath = "C:\\Data\\tools\\jboss\\jboss-datagrid-7.0.0-server\\standalone\\deployments";

    @Test
    public void should_create_deployable() {
        deployableUnit
          .createJar(MyFirstTask.class,
                     TechnicalException.class)
          .deploy("map-reduce.jar", deploymentPath);
    }

}
