package de.scrum_master.performancemonitor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * See https://stackoverflow.com/a/49080647/1082681
 */
public class PerformanceApp {
  public static DefaultPointcutAdvisor createAdvisor(String pointcutExpression, Advice advice) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(pointcutExpression);
    return new DefaultPointcutAdvisor(pointcut, advice);
  }

  public static Object adviseIfNecessary(Object bean, DefaultPointcutAdvisor advisor) {
    final String pointcutExpression = advisor.getPointcut().toString().replaceAll(".*\\(\\) ", "");
    if (!advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
      System.out.println("Pointcut " + pointcutExpression + " does not match class " + bean.getClass());
      return bean;
    }
    System.out.println("Pointcut " + pointcutExpression + " matches class " + bean.getClass() + ", advising");
    Advised advisedBean = createProxy(bean);
    advisedBean.addAdvisor(advisor);
    return advisedBean;
  }

  public static Advised createProxy(Object bean) {
    if (bean instanceof Advised) {
      System.out.println("Bean " + bean + " is already an advised proxy, doing nothing");
      return (Advised) bean;
    }
    System.out.println("Creating proxy for bean " + bean);
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setTarget(bean);
    return (Advised) proxyFactory.getProxy();
  }

  public static void main(String[] args) {
    DefaultPointcutAdvisor advisor = createAdvisor(
      // Just load this from your YAML file as needed
      "execution(public int de.scrum_master.performancemonitor.PersonService.getAge(..))",
      new MyPerformanceMonitorInterceptor(true)
    );

    ApplicationContext context = new AnnotationConfigApplicationContext(AopConfiguration.class);
    Person person = (Person) adviseIfNecessary(context.getBean("person"), advisor);
    PersonService personService = (PersonService) adviseIfNecessary(context.getBean("personService"), advisor);

    System.out.println();
    System.out.println("Name: " + personService.getFullName(person));
    System.out.println("Age: " + personService.getAge(person));
    System.out.println();

    // BTW, you can also unadvise a bean like this.
    // Write your own utility method for it if you need it.
    ((Advised) personService).removeAdvisor(advisor);
    System.out.println("Name: " + personService.getFullName(person));
    System.out.println("Age: " + personService.getAge(person));
  }
}
