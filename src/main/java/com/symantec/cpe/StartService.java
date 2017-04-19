package com.symantec.cpe;

import org.apache.log4j.Logger;
import org.apache.storm.Config;

import com.symantec.cpe.config.ConfigHelper;
import com.symantec.cpe.trident.IdentityTopology;

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
