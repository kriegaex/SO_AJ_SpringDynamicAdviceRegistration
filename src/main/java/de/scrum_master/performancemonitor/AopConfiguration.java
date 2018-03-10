package de.scrum_master.performancemonitor;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;
import java.time.Month;

@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {
  @Pointcut("execution(public String de.scrum_master.performancemonitor.PersonService.getFullName(..))")
  public void monitor() {}

  @Bean
  public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
    return new PerformanceMonitorInterceptor(true);
  }

  @Bean
  public Advisor performanceMonitorAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("de.scrum_master.performancemonitor.AopConfiguration.monitor()");
    return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
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
