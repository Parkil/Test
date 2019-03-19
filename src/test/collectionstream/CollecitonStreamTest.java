package test.collectionstream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**jdk 1.8에서 추가된 stream을 이용하여 collection을 제어하는 예제
 * @author alkain77
 *
 */
public class CollecitonStreamTest {
	public static void main(String[] args) throws Exception{
		//arrayList에서 특정데이터를 필터링해서 가져오는 예제
		List<String> test = Arrays.asList("1","2","3","4","5");
		Stream<String> filterStream = test.stream().filter(val -> val.contains("1")); //안의 val은 람다식에서 사용되는 임시변수. 같은 scope내에 있는 변수와 겹치면 안됨
		
		//System.out.println("filter result count : "+filterStream.count());
		System.out.println("filter result value : "+filterStream.collect(Collectors.toList())); //동일한 stream의 method를 연속으로 호출하면 stream has already been operated upon or closed 예외 발생
		
		
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		
		for(int i = 0 ; i<10 ;i++) {
			Map<String,Object> testMap = new HashMap<String,Object>();
			testMap.put(i+"", "val1");
			testMap.put(i+"a", "val2");
			testMap.put(i+"b", "val3");
			testMap.put(i+"c", "val4");
			testMap.put(i+"d", "val5");
			
			mapList.add(testMap);
		}
		
		/*
		 * stream.map은 stream에 저장된 값을 특정값으로 변환할때 사용한다
		 * ex)
		 * map의 값중 특정값만 뽑아내거나 map(result-> result.get("0a"))
		 * 객체의 hashcode를 표시하거나 map(result-> result.hashCode())
		 */
		//Stream<Map<String,Object>> mapFilterStream = mapList.stream().filter(map -> map.containsKey("0"));
		Stream<Map<String,Object>> mapFilterStream = mapList.stream().filter(map -> map.containsValue("val1"));
		//System.out.println("map filter result count : "+mapFilterStream.count());
		//System.out.println("map filter result : "+mapFilterStream.map(result-> result.get("0a")).collect(Collectors.toList()));
		//System.out.println("map filter result : "+mapFilterStream.collect(Collectors.toSet()));
		List<Map<String, Object>> map = mapFilterStream.collect(Collectors.toList());
		System.out.println(map);
		
		/*
		 * List에 들어있는 중첩리스트 구조를 제거하고 중첩리스트의 요소를 1개의 리스트로 통합
		 * 람다식
		 * 
		 */
		List<List<String>> list = Arrays.asList(Arrays.asList("a"), Arrays.asList("b"));
		List<String> newList = list.stream().flatMap(Collection::stream).collect(Collectors.toList());
		System.out.println(newList);
	}
}
