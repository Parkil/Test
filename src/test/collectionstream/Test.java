package test.collectionstream;

import java.util.List;
import java.util.Map;

@FunctionalInterface
interface Test {
	public boolean isMatch(Map<String, List<String>> orgMap, List<String> clickableElList, List<String> inputableElList);
}
