package com.symantec.cpe.trident;
/**
 * Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
