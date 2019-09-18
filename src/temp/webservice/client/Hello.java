
package temp.webservice.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;



/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 * 해당 소스는 webservice를 구동시키고 [JAVA_HOME]\bin 에서 다음 명령어를 실행시켜 자동으로 생성된 코드임
 * wsimport -d c:\webdown -p temp.webservice.client -keep http://localhost:1212/hello?wsdl
 * 
 * 웹서비스를 가동시키고 [webservice구동시 지정한 url]?wsdl을 웹 브라우저에 입력하면 해당웹서비스의 wsdl를 볼수 있다.
 */
@WebService(name = "Hello", targetNamespace = "http://webservice.temp/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Hello {
    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://webservice.temp/Hello/sayHelloRequest", output = "http://webservice.temp/Hello/sayHelloResponse")
    public String sayHello(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

}
