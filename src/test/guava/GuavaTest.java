package test.guava;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

/*
 * Guava 를 이용한 리스트 분할 예제
 * jdk 1.7이하 버전은 Guava release 20이하버전을 사용해야 함
 */
public class GuavaTest {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		for(int i = 1 ; i<=301 ; i++) {
			list.add(String.valueOf(i));
		}
		
		List<List<String>> partList = Lists.partition(list, 100);
		
		for(List<String> aaa : partList) {
			System.out.println(aaa.size());
		}
	}
}
