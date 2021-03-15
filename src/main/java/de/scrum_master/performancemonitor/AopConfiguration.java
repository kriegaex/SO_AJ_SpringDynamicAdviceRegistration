package de.scrum_master.performancemonitor;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;
import java.time.Month;

@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {
  @Autowired
  MyProperties myProperties;

  @Bean
  public MyPerformanceMonitorInterceptor performanceMonitorInterceptor() {
    return new MyPerformanceMonitorInterceptor();
  }

  @Bean
  @Autowired
  public Advisor performanceMonitorAdvisor(MyPerformanceMonitorInterceptor interceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(myProperties.getPerformanceMonitor());
    return new DefaultPointcutAdvisor(pointcut, interceptor);
  }

  @Bean
  public Person person() {
    return new Person("Albert", "Einstein", LocalDate.of(1871, Month.MARCH, 14));
  }

  @Bean
  public PersonService personService() {
    return new PersonService();
  }
}
