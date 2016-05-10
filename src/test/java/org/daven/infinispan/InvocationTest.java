/*
 * Copyright (c) 2016 Proximus.
 * me@davengeo.com
 */
package org.daven.infinispan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class InvocationTest {

  @Autowired
  Runner runner;

  @Test
  public void should_run_invocation() {

  }

}
