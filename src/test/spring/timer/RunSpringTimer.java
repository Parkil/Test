package test.spring.timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunSpringTimer {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("test/spring/timer/context.xml");
		System.out.println(context.getBeanDefinitionCount());
		System.out.println("�ٸ�����1");
		System.out.println("�ٸ�����2");
		System.out.println("�ٸ�����3");
	}
}
