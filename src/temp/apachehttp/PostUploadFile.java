package temp.apachehttp;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/*
 * apahce http 라이브러리를 이용하여 파일을 업로드하는 예제
 */
public class PostUploadFile {
    public static void main(String[] args) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        FormBodyPart test = FormBodyPartBuilder.create().setName("type").setBody(new StringBody("2", ContentType.DEFAULT_TEXT)).build();
        
        HttpEntity entity = MultipartEntityBuilder
            .create()
            .addPart(test)
            .addBinaryBody("file", new File("c:/webdown/png1.png"), ContentType.create("application/octet-stream"), "png1.png")
            .build();
        
        
        HttpPost httpPost = new HttpPost("https://contents.krt.co.kr/upload/api/tempfile/");
        httpPost.setEntity(entity);
        
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity result = response.getEntity();
        String json = EntityUtils.toString(result, StandardCharsets.UTF_8);
        JSONObject o = new JSONObject(json);
        System.out.println(o.get("file_name"));
        System.out.println(o.get("is_valid"));
    }
}
