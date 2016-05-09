package org.daven.infinispan;

import org.daven.shrinkwrap.DeployableUnit;
import org.junit.Test;

public class DeployableTest {

    DeployableUnit deployableUnit = DeployableUnit.getOne();

    @Test
    public void should_create_greeting() {
        deployableUnit
                .createJar(MyFirstTask.class)
                .deploy("map-reduce.jar");
    }
}
