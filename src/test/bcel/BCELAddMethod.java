package test.bcel;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
/*
 * Method를 새로 생성하고 생성한 메소드를 호출하도록 변경하는 예제
 */
public class BCELAddMethod {
	private JavaClass			jc;
	private ClassGen			cg;
	private ConstantPoolGen		cpg;
	private InstructionFactory	fac;

	//클래스명으로 BCEL초기화(JavaClass,ClassGen,ConstantPoolGen은 웬만한 조작에서 다 들어가며 InstructionFactory의경우 명령어 생성을 용이하게 해주는 클래스임
	private void initBcel(String classname) {
		try {
			jc = Repository.lookupClass("test.bcel.dummy.DummyInt");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cg = new ClassGen(jc);
		cpg = cg.getConstantPool();
		fac = new InstructionFactory(cg, cpg);
	}

	//새로운 method를 생성하고 methodGen을 반환 나중에 cg.addMethod(newmg.getMethod())로직으로 생성한 method를 클래스에 추가함
	MethodGen addMethod() {
		//InstructionList 생성
		InstructionList 	il = new InstructionList();

		//InstructionList에 실제 명령어 입력
		il.append(fac.createPrintln("Puhahaha")); //InstructionFactory를 이용하여 System.out.println명령어 생성후 입력
		il.append(InstructionFactory.createReturn(Type.VOID)); //이부분을 빼먹으면  실행시 Falling off the end of the code 에러가 발생함

		/*
		 * MethodGen - 새로운 메소드를 생성
		 * 1.method가 public/private인지 결정 static인 경우 다음과 같이 처리 Constants.ACC_PUBLIC | Constants.ACC_STATIC
		 * 2.method 리턴타입
		 * 3.인자타입 Type[]
		 * 4.인자이름 String[]
		 * 5.method 이름
		 * 6.클래스명
		 * 7.InstructionList
		 * 8.ConstantPoolGen
		 */

		MethodGen newmg = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[]{}, "puhaha", cg.getClassName(), il, cpg);
		newmg.setInstructionList(il); //method의 명령어를 지정
		newmg.setMaxStack(); //메소드의 max_stack 설정 필요한 stack수보다 max_stack값이 작으면  실행시 Stack size too large 에러가 발생함
		newmg.setMaxLocals(); //메소드의 max_locals설정 (메소드내부의 지역변수개수)
		newmg.removeLineNumbers(); //line number 제거 뭐하는건지는 모르겠음

		return newmg;
	}

	//기존 Mehtod를 추출하여 기존에 있는 메소드호출을 추가
	MethodGen replaceMethod_addref() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		Type[] arguments = Type.getArgumentTypes("()V"); //생성자에 들어가는 인자를 정의

		ALOAD load = new ALOAD(0); //0은 클래스 참조를 저장하는 곳 - non-static 메소드안에서 non-static 메소드를 호출하려면 먼저 이 로직을 호출해야 함
		list.insert(list.getEnd(),load);
		list.insert(list.getEnd(), fac.createInvoke("DummyInt", "puhaha", Type.VOID, arguments, Constants.INVOKEVIRTUAL));

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//기존 Mehtod를 추출하여 지역변수를 선언
	MethodGen replaceMethod_addlocalvar() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		/*
		 * 지역변수를 새로 생성하고 변수에 특정값을 입력
		 * 
		 * MethodGen addLocalVariable
		 * 1.변수 이름
		 * 2.변수 타입 (short,int,long...) 참조형일 경우에는 ObjectType이용
		 * 3.변수 scope(유효범위) 시작
		 * 4.변수 scope(유효범위) 끝
		 */

		//지역변수 생성
		LocalVariableGen lvg = mg.addLocalVariable("t5", Type.STRING, list.getStart(), list.getEnd());
		int lvg_idx = lvg.getIndex();


		LDC ldc = new LDC(cpg.addString("1234567")); //변수에 지정할 값을 입력 short일 경우 SIPUSH,String/int/float의 경우 LDC.. 각 데이터형에 맞는 지정 명령어가 있음
		//SIPUSH ldc = new SIPUSH((short)4000); /
		//ISTORE is = new ISTORE(lvg_idx); //정수형(int,short)일 경우 ISTORE boolean/byte일 경우 BASTORE... 각 데이터 형에 맞는 저장명령어가 있음
		ASTORE is = new ASTORE(lvg_idx); //참조형(기본형인 경우는 사실상 String만 존재)인 경우에는 ASTORE이용

		list.insert(list.getEnd(),ldc);
		list.insert(list.getEnd(),is);

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//기존 Mehtod를 추출하여 지역객체변수 선언하고 객체 생성
	MethodGen replaceMethod_addObjectvar() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		/*
		 * 지역변수를 새로 생성하고 변수에 특정값을 입력
		 * 
		 * MethodGen addLocalVariable
		 * 1.변수 이름
		 * 2.변수 타입 (short,int,long...) 참조형일 경우에는 ObjectType이용
		 * 3.변수 scope(유효범위) 시작
		 * 4.변수 scope(유효범위) 끝
		 */

		//지역객체변수 생성
		ObjectType ot = new ObjectType("Test5"); //객체타입을 지정
		LocalVariableGen lvg = mg.addLocalVariable("t5", ot, list.getStart(), list.getEnd()); //지역변수 생성시 type에 생성한 ObjectType을 지정
		int lvg_idx = lvg.getIndex();

		/*
		 * InsturctionFactory createInvoke
		 * 1.클래스 이름
		 * 2.메소드명		 (잘못 설정시  java.lang.NoSuchMethodError 발생)
		 * 3.메소드리턴타입 (잘못 설정시  java.lang.NoSuchMethodError 발생)
		 * 4.인자(Type 배열) 없을경우 Type.NO_ARGS입력
		 * 5.호출방식 (이게 잘못설정될경우  클래스 파일 실행시 Unable to pop operand off an empty stack 에러가 발생한다 각별한 주의 요망)
		 * 	5-1.INVOKESTATIC  : static method 호출
		 * 	5-2.INVOKEVIRTUAL : non - static method 호출
		 * 	5-3.INVOKESPECIAL : 상위클래스의 method 를 호출하거나 private 또는 instance 초기화 method 호출
		 */

		//객체 생성 new Test5();
		Type[] arguments = Type.getArgumentTypes("()V"); //생성자에 들어가는 인자를 정의
		list.insert(list.getEnd(), fac.createNew("Test5"));
		list.insert(list.getEnd(), InstructionConstants.DUP); //생성자 인자가 없을때에는 InstructionConstants.DUP 있으면 객체타입에 맞는 인자를 입력 - 이부분이 잘못입력되면 클래스 파일 실행시 Expecting to find object/array on stack 에러가 발생
		list.insert(list.getEnd(), fac.createInvoke("Test5", "<init>", Type.VOID, arguments, Constants.INVOKESPECIAL));

		ASTORE is = new ASTORE(lvg_idx); //객체를 초기화 해서 생성한 지역변수에 저장 참조형인 경우에는 ASTORE이용
		list.insert(list.getEnd(), is);

		ALOAD load = new ALOAD(lvg_idx); //초기화된 객체를 저장한 지역변수를 load
		list.insert(list.getEnd(), load);
		list.insert(list.getEnd(), fac.createInvoke("Test5", "a", Type.VOID, arguments, Constants.INVOKEVIRTUAL)); //Test5 t5 =

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//클래스에서 인자로주어진 이름에 해당하는 메소드 객체를 반환
	private Method getMethod(String methodname) {
		Method target = null;
		for(Method m : cg.getMethods()) {
			if(m.getName().intern() == methodname.intern()) {
				target = m;
				break;
			}
		}

		return target;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		BCELAddMethod bcel = new BCELAddMethod();
		bcel.initBcel("DummyInt");

		Method target = bcel.getMethod("methodA");
		bcel.cg.addMethod(bcel.addMethod().getMethod());
		bcel.cg.replaceMethod(target, bcel.replaceMethod_addref().getMethod());
		bcel.cg.getJavaClass().dump("d:/DummyInt.class");
	}
}
