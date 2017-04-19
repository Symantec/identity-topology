package com.symantec.cpe.spout;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.log4j.Logger;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.spout.RawScheme;
import org.apache.storm.spout.Scheme;

import com.symantec.cpe.config.Constants;

public class SchemeBuilder {
  private static final Logger LOG = Logger.getLogger(SchemeBuilder.class);

  private SchemeBuilder() {

  }

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

  public static Class<? extends Deserializer<?>> getDeserializer(String schemeType) {
    if (schemeType == null || schemeType.length() == 0) {
      LOG.error("Input Scheme is wrong");
      return null;
    }
    
    if (schemeType.toLowerCase().contains(Constants.RAW_STRING)) {
      return org.apache.kafka.common.serialization.ByteArrayDeserializer.class;
    } else {
      return org.apache.kafka.common.serialization.StringDeserializer.class;
    }
  }

}
