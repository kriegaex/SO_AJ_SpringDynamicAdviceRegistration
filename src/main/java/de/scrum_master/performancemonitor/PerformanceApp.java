package de.scrum_master.performancemonitor;

import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * See https://stackoverflow.com/a/49080647/1082681
 */
public class PerformanceApp {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AopConfiguration.class);
    Person person = (Person) context.getBean("person");
    PersonService personService = (PersonService) context.getBean("personService");

    System.out.println("Name: " + personService.getFullName(person));
    System.out.println("Age: " + personService.getAge(person));
    System.out.println();

    System.out.println("Unadvising PersonService bean");
    Arrays.stream(((Advised) personService).getAdvisors())
      .filter(advisor -> advisor.getAdvice() instanceof MyPerformanceMonitorInterceptor)
      .findFirst()
      .ifPresent(((Advised) personService)::removeAdvisor);

    System.out.println("Name: " + personService.getFullName(person));
    System.out.println("Age: " + personService.getAge(person));
  }
}
