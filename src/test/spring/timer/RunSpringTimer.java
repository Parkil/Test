package test.spring.timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunSpringTimer {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("test/spring/timer/context.xml");
		System.out.println(context.getBeanDefinitionCount());
		System.out.println("다른업무1");
		System.out.println("다른업무2");
		System.out.println("다른업무3");
	}
}
