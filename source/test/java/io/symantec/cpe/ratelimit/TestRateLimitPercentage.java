package io.symantec.cpe.ratelimit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRateLimitPercentage {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @Before
  public void setUp() throws Exception {}



  @Test
  public void testSimple() {
    com.symantec.cpe.ratelimit.RateLimiterPercentage percentage =
        new com.symantec.cpe.ratelimit.RateLimiterPercentage(2);
    assertTrue(percentage.percentCount());
    assertTrue(percentage.percentCount());
    assertFalse(percentage.percentCount());
  }
  
  @Test
  public void testLonger() {
    com.symantec.cpe.ratelimit.RateLimiterPercentage percentage =
        new com.symantec.cpe.ratelimit.RateLimiterPercentage(20);
    for(int i=0;i<20;i++){
      assertTrue(percentage.percentCount());
    }
    for(int i=0;i<80;i++){
      assertFalse(percentage.percentCount());
    }
  }
  
  @Test
  public void testPercentFull() {
    com.symantec.cpe.ratelimit.RateLimiterPercentage percentage =
        new com.symantec.cpe.ratelimit.RateLimiterPercentage(100);
    for(int i=0;i<100;i++){
      assertTrue(percentage.percentCount());
    }
     
  }



  @Test(expected = IllegalArgumentException.class)
  public void testException() {
    @SuppressWarnings("unused")
    com.symantec.cpe.ratelimit.RateLimiterPercentage percentage =
        new com.symantec.cpe.ratelimit.RateLimiterPercentage(-1);

  }
  
  public void testZero() {
    com.symantec.cpe.ratelimit.RateLimiterPercentage percentage =
        new com.symantec.cpe.ratelimit.RateLimiterPercentage(0);
    for(int i=0;i<100;i++){
      assertFalse(percentage.percentCount());
    }

  }



}
