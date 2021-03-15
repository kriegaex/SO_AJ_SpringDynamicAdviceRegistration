package de.scrum_master.performancemonitor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pointcuts")
public class MyProperties {
  private String performanceMonitor;

  public String getPerformanceMonitor() {
    return performanceMonitor;
  }

  public void setPerformanceMonitor(String performanceMonitor) {
    this.performanceMonitor = performanceMonitor;
  }
}
