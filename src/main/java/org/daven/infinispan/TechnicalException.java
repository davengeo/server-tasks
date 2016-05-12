/*
 * Copyright (c) 2016 Proximus.
 * me@davengeo.com
 */
package org.daven.infinispan;


public class TechnicalException extends RuntimeException {

  public TechnicalException() {
    super("TechnicalException has occurred");
  }

  public TechnicalException(Throwable t) {
    super(t);
  }

  public TechnicalException(String message) {
    super(message);
  }

  public TechnicalException(String message, Throwable t) {
    super(message, t);
  }
}
