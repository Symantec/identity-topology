package com.symantec.cpe.ratelimit;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

import com.symantec.cpe.config.DO.RateLimitConfigDO;

public class RateLimiterPercentage extends BaseFilter {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;
 
  // default 100%
  private int maxPercent = RateLimitConfigDO.PERCENTAGE_FULL;
  // current is 0
  private int counter = 0;

  public RateLimiterPercentage(int maxPercent) {
    if (maxPercent < 0 || maxPercent > RateLimitConfigDO.PERCENTAGE_FULL)
      throw new IllegalArgumentException("Count must be positive and less then " + Long.MAX_VALUE);
    this.maxPercent = maxPercent;
  }


  public RateLimiterPercentage(RateLimitConfigDO rateLimitConfig) {
    this.maxPercent = rateLimitConfig.getRatePercentage();
     
  }


  public boolean percentCount() {
    // increment the counter, pre increment
    counter++;

    if (counter <= maxPercent) {
      return true;
    } else {
      if (counter == RateLimitConfigDO.PERCENTAGE_FULL) {
        // reset on reaching 100
        counter = 0;
      }
      // drop all messages between maxPercent and PERCENTAGE_FULL
      return false;
    }
  }


  @Override
  public boolean isKeep(TridentTuple tuple) {
    if(percentCount()){
      return true;
    }else{
      return false;
    }
  }

}
