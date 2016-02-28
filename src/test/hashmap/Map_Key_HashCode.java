package test.hashmap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * map���� key�� object�� ����ϴ� ����
 * 
 * ���� String���� map.put("111","222");���� ����ϰԵǴµ� Stringó�� �����ϴ� Case�� �ƴ� new�� �̿��Ͽ� 
 * ���� �����ϴ� object�� key�� ����� ��� ������ ���� ������ �߻��� ������ �ִ�.
 * 
 * key�� A�� class�� ����� ���(A class���� String a; int b; float c;�� �ִٰ� ����)
 * 
 * A a = new A("1", 2, 0.2);
 * 
 * map.put(a, "1111");
 * String val = map.get(new A("1", 2, 0.2));
 * 
 * val�� 1111�� ���ð� ������ null�� ǥ�õȴ�. 
 * Map�������̽��� ������  Ŭ����(map_class��� ��Ī)�� key�� Object�� ����� ��� map_class�� get(Object)ȣ��� ���ڷ� ���� Object��
 * map_class ������ key�� �˻��ϴ� ������ ������ ����
 * 1) ���ڷ� ���� Object�� hashCode()�� ȣ���Ͽ� ���� ���� �������� Map������ hash bucket(map ���ο��� �˻��� ������ �ϱ� ���� hashcode ������� ����Ҹ� �и��Ѱ�)�� ã�´�
 * 2) 1������ ã�� hash bucket�� ����ִ� �����͸� equals�� �̿��Ͽ� �����͸� �˻��Ѵ�.
 * 
 * Object �⺻ hashcode�� equals�� ��쿡�� new�� �ű� ������ ��ü�� ���� hashcode�� �ٸ��� equals�� false�� ��ȯ�ϱ� ������
 * null�� ǥ�ð� �Ǹ� �̸� �����ϱ� ���ؼ� hashcode,equals�� ������ �� �ʿ䰡 �ִ�.
 * 
 * hahscode ������ �ϴ� ��
 * 1.int ������ 0�� �ƴ� ���� �Է�
 * 2.class�� ������ ���� hashcode����
 * 2.1 boolean - (boolean ? 0 : 1)
 * 2.2 byte,char,short,int - (int)f
 * 2.3 long - (int)(long ^ (long >> 32))
 * 2.4 float - Float.floatToIntBits(float)
 * 2.5 double - Double.doubleToLongBits(double) -> (int)(long ^ (long >> 32))
 * 2.6 object - object.hashCode()
 * 3. 37 * (1����) + (2����) �� ����������ŭ �ݺ�
 * 
 * equals�� ��ü�� �������� ���� ���ϰų� �Ǵ� ��ü instanceof + hashcode�� ���ϴ� ������� �������Ѵ�.
 * 
 * �׸��� ConcurrentSkipListMap���� key�� Comparator or Comparable�� �䱸�ϴ� ��쿡�� hashcode,equals�� ���� ��� compareTo,compare�� ���� 0�� ��ȯ�ؾ� �Ѵ�
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
