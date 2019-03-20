package test.collectionstream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamCompareLists {
	public static void main(String[] args) {
		List<String> compareA = new ArrayList<String>();
		List<String> compareB = new ArrayList<String>();
		
		compareA.add("1");
		compareA.add("2");
		compareA.add("3");
		compareA.add("4");
		compareA.add("5");
		
		compareB.add("a");
		compareB.add("b");
		compareB.add("c");
		compareB.add("d");
		compareB.add("e");
		
		List<String> targetA = new ArrayList<String>();
		List<String> targetB = new ArrayList<String>();
		
		//targetA.add("6");
		targetA.add("6");
		targetA.add("1");
		targetA.add("2");
		targetA.add("3");
		targetA.add("4");
		
		targetB.add("a");
		targetB.add("b");
		targetB.add("c");
		targetB.add("d");
		targetB.add("e");
		
		System.out.println(compareA.equals(targetA));
		
		Stream<String> comA = compareA.stream();
		
		boolean chkResult = comA.allMatch(t -> targetA.contains(t) && targetA.size() == 5);
		System.out.println("chkResult : "+chkResult);
		
		/*
		 * stream을 이용한 정렬처리
		System.out.println(targetA);
		System.out.println(targetA.stream().sorted().collect(Collectors.toList()));
		*/
		
		Map<String,List<String>> listMap = new HashMap<String,List<String>>();
		listMap.put("a", Arrays.asList("1","2","3","4","5"));
		listMap.put("b", Arrays.asList("a","b","c","d","e"));
		
		List<Map<String,List<String>>> testList = new ArrayList<Map<String,List<String>>>();
		testList.add(listMap);
		
		Test matchFunc = (Map<String, List<String>> orgMap, List<String> clickableElList, List<String> inputableElList) -> {
			return orgMap.size() == 0;
		};
		
		Stream<Map<String, List<String>>> testStream = testList.stream();
		testStream.allMatch(a -> a.size() == 0);
	}
}
