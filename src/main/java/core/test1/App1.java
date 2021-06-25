package core.test1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App1 {
	public static void main(String[] args) {
		System.out.println("프로그램 실행1");
		
//		Desk desk = new Desk();
//		이런 일들을 스프링이 대신 한다.
	
		String path = "core/test1/core-test1.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(path);
		//어떤 방식으로 사용할지명시를 해야하는데
		//설명서는 xml 또는 java 파일로 작성해야한다.
		
		Object o = context.getBean("desk");
		System.out.println(o);

		Object o2 = context.getBean("leg");
		System.out.println(o2);
		
		Object o3 = context.getBean("chair");
		System.out.println(o3);
	}
}
