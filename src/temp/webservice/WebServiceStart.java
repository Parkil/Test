package temp.webservice;

import javax.xml.ws.Endpoint;

/*
 * web service를 가동
 * Endpoint.publish(url, new Hello());를 이용하여 webservice를 특정 url에 mapping시키고(url은 사용자 임의대로 설정) 기동시킨다.
 */
public class WebServiceStart {
    public static void main(String[] args) {
        String url = "http://localhost:1212/hello";
        Endpoint.publish(url, new Hello());
        System.out.println("Service started @ " + url);
    }
}
