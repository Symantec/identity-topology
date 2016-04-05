package com.symantec.cpe.config;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import backtype.storm.Config;

public class ConfigHelper {

  /** The Constant LOG. */
  private static final Logger LOG = Logger.getLogger(ConfigHelper.class);

  /*
   * A HashMap containing configurations property names and their corresponding
   * data types
   */
  /** The Constant keyTypeMap. */
  @SuppressWarnings("rawtypes")
  private static final Map<String, Class> keyTypeMap = new HashMap<String, Class>();

  /**
   * Load config: open to user, call a private function loadConfigFromFile.
   */
  public static Config loadConfig(String propertiesFileName)  {
    try{
      return loadConfigFromFile(propertiesFileName);
    }catch(Exception e){
      LOG.error("Failed while loading properties from file " + propertiesFileName, e);
      return null;
    }

  }

  /**
   * Load config from file: implement the function loadConfig, blind to users.
   */
  /**
   * Load config from file: implement the function loadConfig, blind to users.
   */
  private static Config loadConfigFromFile(String propertiesFileName) throws IOException {

    Config conf = new Config();

    try {
      // Load configs from properties file
      LOG.debug("Loading properties from " + propertiesFileName);
      InputStream fileInputStream = new FileInputStream(propertiesFileName);
      Properties properties = new Properties();
      try {
        properties.load(fileInputStream);
      } finally {
        fileInputStream.close();
      }

      for (@SuppressWarnings("rawtypes") Map.Entry me : properties.entrySet()) {
        try {
          // Check every property and covert to appropriate data type before
          // adding it to storm config. If no data type defined keep it as string
          if (keyTypeMap.containsKey(me.getKey().toString())) {
            if (Number.class.equals(keyTypeMap.get(me.getKey().toString()))) {
              conf.put((String) me.getKey(), Long.valueOf(((String) me.getValue()).trim()));
            } else if (Map.class.equals(keyTypeMap.get(me.getKey().toString()))) {
              if (me.getValue() != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                String kvPairs[] = StringUtils.split(me.getValue().toString(), properties.getProperty("map.kv.pair.separator", ","));
                for (String kvPair : kvPairs) {
                  String kv[] = StringUtils.split(kvPair, properties.getProperty("map.kv.separator", "="));
                  map.put((String) kv[0], kv[1].trim());
                }
                conf.put((String) me.getKey(), map);
              }
            } else if (Boolean.class.equals(keyTypeMap.get((String) me.getKey()))) {
              conf.put((String) me.getKey(), Boolean.valueOf(((String) me.getValue()).trim()));
            }
          } else {
            conf.put(me.getKey().toString(), me.getValue());
          }
        } catch (NumberFormatException nfe) {
          LOG.error("Failed while loading properties from file " + propertiesFileName + ". The value '" + me.getValue()
          + "' for property " + me.getKey() + " is expected to be numeric", nfe);
          throw nfe;
        }
      }
    } catch (IOException ioe) {
      LOG.error("Failed while loading properties from file " + propertiesFileName, ioe);
      throw ioe;
    }
    return conf;
  }

}
