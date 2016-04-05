package com.symantec.cpe;
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

import com.symantec.cpe.config.ConfigHelper;
import com.symantec.cpe.trident.IdentityTopology;

import backtype.storm.Config;

/**
 * Main Class to start the Reading and Writing This program reads the data from the Kafka End point
 * via Zookeeper setting and writes it back to new Kafka End point using Trident topology
 */
public class StartService {
  private static final Logger LOG = Logger.getLogger(StartService.class);

  public static void main(String[] args) {
    // Read Property
    // <=1 error

    if (args == null || args.length < 1) {
      LOG.error("Input Validation failed, Below is the signature ");
      LOG.error(
          "USAGE : storm Jar " + StartService.class.getName() + "  <configuration file path>");
      return;
    }
 
    // 0
    String propertyFile = args[args.length-1];
    LOG.info("File Path \t " + propertyFile);



    /* Get configurations */
    Config inputPropertyConf = ConfigHelper.loadConfig(propertyFile);

    if (inputPropertyConf == null) {
      LOG.error("Error in the input Property Configuration");
      return;
    }

    // Run
    IdentityTopology.buildToplogyAndSubmit(inputPropertyConf);

  }

}
