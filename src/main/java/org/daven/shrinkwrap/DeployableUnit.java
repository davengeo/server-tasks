package org.daven.shrinkwrap;

import org.infinispan.tasks.ServerTask;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import java.io.File;

public class DeployableUnit {

    private JavaArchive javaArchive;

    protected DeployableUnit() {
    }

    public static DeployableUnit getOne() {
        return new DeployableUnit();
    }

    public DeployableUnit createJar(Class... serviceClasses) {

        javaArchive = ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(serviceClasses)
                .addAsServiceProvider(ServerTask.class, serviceClasses[0]);

        return this;
    }

    public void deploy(String fileName, String deployPath) {
        File f = new File(deployPath, fileName);
        javaArchive.as(ZipExporter.class).exportTo(f, true);
    }


}
