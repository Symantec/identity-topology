package com.symantec.cpe.config.DO;

public class RateLimitConfigDO {

  public static final int PERCENTAGE_FULL = 100;
  
  private int parallelCount;
  private RateType rateType;
  private int ratePercentage;
  private long rateValue;

  public int getParallelCount() {
    return parallelCount;
  }

  public void setParallelCount(int parallelCount) {
    
    this.parallelCount = parallelCount;
  }

  public RateType getRateType() {
    return rateType;
  }

  public void setRateType(RateType rateType) {
    this.rateType = rateType;
  }
  
  public void setRateType(String str){
    if(str!=null && !str.isEmpty() ){
      if(str.compareToIgnoreCase("percent")==0){
        setRateType(RateType.PERCENTAGE);
      }else{
        setRateType(RateType.VALUE);
      }
    }
  }

  public int getRatePercentage() {
    return ratePercentage;
  }

  public void setRatePercentage(int ratePercentage) {
    if (ratePercentage < 0 || ratePercentage > PERCENTAGE_FULL)
      throw new IllegalArgumentException("Count must be positive and less then " + Long.MAX_VALUE);
    
    this.ratePercentage = ratePercentage;
  }

  public long getRateValue() {
    return rateValue;
  }

  public void setRateValue(long rateValue) {
    this.rateValue = rateValue;
  }



}
