package com.symantec.cpe.config;

import org.apache.log4j.Logger;

public enum TYPE {

  INPUT, OUTPUT;
  private static final Logger LOG = Logger.getLogger(TYPE.class);

  public static TYPE getType(String typeString) {
    switch (typeString.toUpperCase()) {
      case "INPUT":
        return TYPE.INPUT;
      case "OUTPUT":
        return TYPE.OUTPUT;
    }
    LOG.error("ENUM ERROR " + typeString);
    return null;

  }
}
