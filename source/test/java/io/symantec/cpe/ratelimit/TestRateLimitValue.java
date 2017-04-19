package io.symantec.cpe.ratelimit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRateLimitValue {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @Before
  public void setUp() throws Exception {}


  @Test
  public void test1() {
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(2, 1);

    assertTrue(valueLimiter.numberCount(System.currentTimeMillis()));
    assertTrue(valueLimiter.numberCount(System.currentTimeMillis()));
    assertFalse(valueLimiter.numberCount(System.currentTimeMillis()));
  }


  @Test
  public void test2() {
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(1, 1);

    assertTrue(valueLimiter.numberCount(System.currentTimeMillis()));
    assertFalse(valueLimiter.numberCount(System.currentTimeMillis()));
  }

  @Test
  public void testRepeat() {
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(1, 1);

    assertTrue(valueLimiter.numberCount(System.currentTimeMillis()));
    assertFalse(valueLimiter.numberCount(System.currentTimeMillis()));
    // send the message in next slot by adding a minute
    assertTrue(valueLimiter.numberCount(System.currentTimeMillis() + 1000 * 60+1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException() {

    @SuppressWarnings("unused")
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(-1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {

    @SuppressWarnings("unused")
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException3() {

    @SuppressWarnings("unused")
    com.symantec.cpe.ratelimit.RateLimiterValue valueLimiter =
        new com.symantec.cpe.ratelimit.RateLimiterValue(1, 2);
  }


}
