package temp;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.auth0.jwt.internal.org.apache.commons.lang3.RandomStringUtils;

import test.url.JavaPost;

public class GenerateRandomString {
	public static void main(String[] args) throws Exception{
		System.out.println("11111111111".length());
	    int length = 12;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	    System.out.println(generatedString);
	    
	    String generatedString1 = RandomStringUtils.randomAlphabetic(10);
	    System.out.println(generatedString1);
	    
	    
	    String generatedString2 = RandomStringUtils.randomAlphanumeric(12);
	    System.out.println(generatedString2);
	}
}
