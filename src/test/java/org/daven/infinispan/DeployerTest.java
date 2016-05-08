package org.daven.infinispan;

import org.infinispan.tasks.ServerTask;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(Arquillian.class)
@RunAsClient
public class DeployerTest {

    public static JavaArchive createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClass(MyFirstTask.class)
                .addAsServiceProvider(ServerTask.class, MyFirstTask.class);

    }

    @Test
    public void should_create_greeting() {
        final JavaArchive javaArchive = createDeployment();
        File f = new File(".", "custom-distributed-task.jar");
        javaArchive.as(ZipExporter.class).exportTo(f, true);
    }
}
