package temp.webservice.client;

/*
 * webservice를 call하는 예제
 */
public class WebServiceCall {
    public static void main(String[] args) {
        HelloService service = new HelloService();
        Hello hello = service.getHelloPort();
        String text = hello.sayHello("테스트");
        System.out.println(text);
    }
}
