package de.scrum_master.performancemonitor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class MyPerformanceMonitorInterceptor implements MethodInterceptor {
  protected transient Log log = LogFactory.getLog(getClass());

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    String name = invocation.getMethod().toString();
    long start = System.currentTimeMillis();
    log.info("Method " + name + " execution started at: " + new Date());
    try {
      return invocation.proceed();
    } finally {
      long end = System.currentTimeMillis();
      long time = end - start;
      log.info("Method " + name + " execution lasted: " + time + " ms");
      log.info("Method " + name + " execution ended at: " + new Date());
      if (time > 10) {
        log.warn("Method execution longer than 10 ms!");
      }
    }
  }
}
