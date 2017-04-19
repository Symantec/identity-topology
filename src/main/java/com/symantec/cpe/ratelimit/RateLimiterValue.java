package com.symantec.cpe.ratelimit;

import org.apache.log4j.Logger;
import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

import com.symantec.cpe.config.DO.RateLimitConfigDO;

public class RateLimiterValue extends BaseFilter {

  /**
   * 
   */
  private static final long serialVersionUID = -1266084428169189933L;
  private long maxEvents = 100;
  private long nextTimeInterval = 0;
  private long eventCounter = 0;
  private static final Logger LOG = Logger.getLogger(RateLimiterValue.class);

  public RateLimiterValue(long maxEvents, int spoutParallelHint) {
    // check if max is zero or less
    if (maxEvents < 0)
      throw new IllegalArgumentException("Count must be positive and less then " + Long.MAX_VALUE);

    // Spout Parallelism check
    if (spoutParallelHint < 1)
      throw new IllegalArgumentException(
          "Spout Parallelism  must be positive " + spoutParallelHint);
    this.maxEvents = maxEvents / spoutParallelHint;
    if (this.maxEvents < 1)
      throw new IllegalArgumentException(
          "MaxEvents / spoutParallelHint  must be positive and greater than zero " + this.maxEvents
              + "/" + spoutParallelHint);
    this.nextTimeInterval = System.currentTimeMillis() + (long) getInterval();
    this.eventCounter = 0;
  }


  public RateLimiterValue(RateLimitConfigDO rateLimitConfig, int spoutParallelHint) {
    if (maxEvents < 0)
      throw new IllegalArgumentException("Count must be positive and less then " + Long.MAX_VALUE);

    if (spoutParallelHint < 1)
      throw new IllegalArgumentException(
          "Spout Parallelism  must be positive " + spoutParallelHint);

    this.maxEvents = rateLimitConfig.getRateValue() / spoutParallelHint;
    if (this.maxEvents < 1)
      throw new IllegalArgumentException(
          "MaxEvents / spoutParallelHint  must be positive and greater than zero " + this.maxEvents
              + "/" + spoutParallelHint);
    this.nextTimeInterval = System.currentTimeMillis() + (long) getInterval();
    this.eventCounter = 0;
  }



  public boolean numberCount(long currentTime) {
    // counter increments
    eventCounter++;
    // Time limit
    if (currentTime <= nextTimeInterval) {
      // Check if hit Max
      if (eventCounter <= maxEvents) {
        return true;
      } else {
        // do nothing below false return will take care
        return false;
      }
    } else {
      nextTimeInterval = getInterval(nextTimeInterval, currentTime);
      // reset and increment
      eventCounter = 1;
      return true;
    }
  }


  private long getInterval(long currentInterval, long currentTime) {
    if (currentTime <= currentInterval) {
      return currentInterval + (long) getInterval();
    } else {
      // Go the right interval not just next interval, calculate the Value.
      // delta should always be greater or equal to 1
      long delta = (long) (Math.ceil((currentTime - currentInterval) / (getInterval())));
      LOG.info("Delta " + delta);
      return currentInterval + (long) getInterval() * delta;
    }

  }

  private double getInterval() {
    return 1000.0 * 60.0;
  }



  @Override
  public boolean isKeep(TridentTuple tuple) {
    if (numberCount(System.currentTimeMillis())) {
      return true;
    } else {
      return false;
    }
  }



}
