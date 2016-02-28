package test.hashmap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * map에서 key를 object로 사용하는 예제
 * 
 * 보통 String으로 map.put("111","222");으로 사용하게되는데 String처럼 정의하는 Case가 아닌 new를 이용하여 
 * 새로 생성하는 object를 key로 사용할 경우 다음과 같은 문제가 발생할 소지가 있다.
 * 
 * key를 A란 class로 사용할 경우(A class에는 String a; int b; float c;가 있다고 가정)
 * 
 * A a = new A("1", 2, 0.2);
 * 
 * map.put(a, "1111");
 * String val = map.get(new A("1", 2, 0.2));
 * 
 * val에 1111이 나올거 같지만 null이 표시된다. 
 * Map인터페이스를 구현한  클래스(map_class라고 지칭)의 key를 Object로 사용할 경우 map_class가 get(Object)호출시 인자로 들어온 Object로
 * map_class 내부의 key를 검색하는 로직은 다음과 같다
 * 1) 인자로 들어온 Object의 hashCode()를 호출하여 나온 값을 기준으로 Map내부의 hash bucket(map 내부에서 검색을 빠르게 하기 위해 hashcode 기반으로 저장소를 분리한것)을 찾는다
 * 2) 1번에서 찾은 hash bucket에 들어있는 데이터를 equals를 이용하여 데이터를 검색한다.
 * 
 * Object 기본 hashcode와 equals의 경우에는 new로 신규 생성한 객체는 각각 hashcode가 다르고 equals도 false를 반환하기 때문에
 * null이 표시가 되며 이를 방지하기 위해서 hashcode,equals를 재정의 할 필요가 있다.
 * 
 * hahscode 재정의 하는 법
 * 1.int 변수에 0이 아닌 값을 입력
 * 2.class의 변수에 따라서 hashcode지정
 * 2.1 boolean - (boolean ? 0 : 1)
 * 2.2 byte,char,short,int - (int)f
 * 2.3 long - (int)(long ^ (long >> 32))
 * 2.4 float - Float.floatToIntBits(float)
 * 2.5 double - Double.doubleToLongBits(double) -> (int)(long ^ (long >> 32))
 * 2.6 object - object.hashCode()
 * 3. 37 * (1번값) + (2번값) 를 변수개수만큼 반복
 * 
 * equals는 객체의 변수끼리 직접 비교하거나 또는 객체 instanceof + hashcode를 비교하는 방식으로 재정의한다.
 * 
 * 그리고 ConcurrentSkipListMap같이 key에 Comparator or Comparable를 요구하는 경우에는 hashcode,equals가 같을 경우 compareTo,compare의 값이 0을 반환해야 한다
 */
class Sub implements Comparable<Sub>{
	private String aaa;
	
	public Sub(){}
	public Sub(String aaa) {
		this.aaa = aaa;
	}
	
	public void set(String aaa) {
		this.aaa = aaa;
	}
	
	public String get() {
		return aaa;
	}
	
	@Override
	public int compareTo(Sub arg0) {
		return arg0.hashCode() - this.hashCode();
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + aaa.hashCode(); 
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		
		if(obj instanceof Sub == false) {
			return false;
		}
		
		Sub s = (Sub)obj;
		return s.aaa == aaa;
	}
}


public class Map_Key_HashCode {
	//private final 
	
	public static void main(String[] args) {
		//Map<Object,String> aaa = new HashMap<Object,String>();
		
		//Map<Object,String> aaa = new ConcurrentSkipListMap<Object,String>();
		ConcurrentMap<Object,String> aaa = new ConcurrentHashMap<Object,String>();
		
		Sub a1 = new Sub();
		a1.set("111");
		
		Sub a2 = new Sub();
		a2.set("111");
		
		System.out.println(a1.hashCode());
		System.out.println(a2.hashCode());
		System.out.println(a1.equals(a2));
		
		aaa.put(a1, "a1-value");
		System.out.println(aaa.get(a1));
		
		for(Object key : aaa.keySet()) {
			System.out.println(key);
			System.out.println( ((Sub)key).get() );
			System.out.println(new Sub("111").equals(key));
		}
	}
}
