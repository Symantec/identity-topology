package com.symantec.cpe.trident;

import org.apache.log4j.Logger;
import com.symantec.cpe.config.Constants;
import backtype.storm.spout.RawScheme;
import backtype.storm.spout.Scheme;
import storm.kafka.StringScheme;

public class SchemeBuilder {
  private static final Logger LOG = Logger.getLogger(SchemeBuilder.class);

  private static Scheme getRawScheme() {
    return new RawScheme();
  }

  private static Scheme getStringScheme() {
    return new StringScheme();
  }

  public static Scheme getScheme(String str) {
    if (str == null || str.length() == 0) {
      LOG.error("Input Scheme is wrong");
      return null;
    }
    if (str.toLowerCase().contains(Constants.RAW_STRING)) {
      return getRawScheme();
    } else {
      return getStringScheme();
    }
  }

}
