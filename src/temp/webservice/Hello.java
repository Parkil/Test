package temp.webservice;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/*
 * Soap기반의 java web service를 구성하는 예제
 * 
 * 일반적인 class를 생성하고 @WebService와 @SOAPBinding(style=SOAPBinding.Style.RPC) annotaion을 설정하면 해당 클래스는 웹 서비스로 구성이 된다.
 */
@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class Hello {
    public String sayHello(String name) {
        System.out.println("Soap RPC Call param : "+name);
        return "Hello " + name;
    }
}