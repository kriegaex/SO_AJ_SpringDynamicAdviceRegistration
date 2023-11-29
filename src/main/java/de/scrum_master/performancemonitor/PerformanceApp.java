package de.scrum_master.performancemonitor;

import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * See <a href="https://stackoverflow.com/a/49080647/1082681">StackOverflow q49072611</a><p>
 * See <a href="https://stackoverflow.com/a/66637724/1082681">StackOverflow q66535042</a>
 */
@SpringBootApplication
@EnableConfigurationProperties(MyProperties.class)
public class PerformanceApp {
  public static void main(String[] args) {
    try (ConfigurableApplicationContext context = SpringApplication.run(PerformanceApp.class, args)) {
      doStuff(context);
    }
  }

  private static void doStuff(ConfigurableApplicationContext context) {
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
