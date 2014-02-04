package test.bcel;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

/*
 * Method의 index를 찾는 예제
 */

public class BCELlookupmethod {
	public static void main(String[] args) throws Exception{
		String basepath = new BCELlookupmethod().getClass().getResource(".").getPath();
		ClassParser cp = new ClassParser(basepath+"DummyInt.class");

		ClassGen cg = new ClassGen(cp.parse());
		System.out.println("class name idx : "+cg.getClassNameIndex());

		Method temp = null;
		for(Method m : cg.getMethods()) {
			if(m.getName().intern() == "main".intern()) {
				temp = m;
			}

			/*
			if(m.getName().intern() == "methodB".intern()) {
				name_idx = m.getNameIndex();
				System.out.println("methodB name index : "+m.getNameIndex());
			}*/
		}

		ConstantPoolGen cpg	 = cg.getConstantPool();
		MethodGen		mg	 = new MethodGen(temp, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();


		/*
		 * lookupMethodref - ConstantPoolGen에서 Method index를 반환한다 index는 나중에 INVOKEVIRTUAL 생생시 인자로 들어갈수 있다.
		 * 					 methodref는 대상 class에서 호출된 method만 검색을 한다. 호출되지 않은 method를 검색하려면
		 * 					 ConstantPoolGen에서 cpg.addMethodref(cg.getClassName(), "methodB", "(II)V")를 호출하여야 한다.
		 * 					 (클래스명, 메소드명, signature) - Method index를 반환함
		 * 
		 * Parameter
		 * 1.lookupMethodref(MethodGen gen)
		 *   - MethodGen Instance
		 *
		 * 2.lookupMethodref(String classname, String methodname, String signature);
		 *   - classname 클래스명
		 *   - methodname 메소드명
		 *   - signature 메소드인자 및 return type
		 *   =기본적인 return type=
		 *   B	byte
			 C	char
			 D	double
			 F	float
			 I	int
			 J	long
			 L<classname>;	reference to a class
			 S	short
			 Z	boolean
			 [	array dimension
			 V	void

		 *   ex) 인자가 없고 void형 리턴
		 *   "()V"
		 * 
		 *   ex) 문자열 인자를 받고 void형 리턴
		 *   "(Ljava/lang/String;)V
		 * 
		 *   ex) int 인자 2개 void형 리턴
		 *   (II)V
		 * 
		 *   잘 모를경우 다음 로직 실행
		 * 
		 *   Type param[] = {Type.INT,Type.INT};
		 *   //앞인자 - 리턴 타입 뒤인자 - 파라메터
			 System.out.println(Type.getMethodSignature(Type.VOID,param));
		 */
		String className	= cg.getClassName();
		String methodName	= "methodA";
		int newidx = cpg.lookupMethodref(className, methodName, "(II)V");
		System.out.println("newidx : "+newidx);

		newidx = cpg.addMethodref(cg.getClassName(), "methodB", "(II)V");

		//		newidx = cpg.lookupMethodref(className, "methodB", "(II)V");
		System.out.println("newidx2 : "+newidx);

		InstructionHandle target = null;

		for(InstructionHandle handle : list.getInstructionHandles()) {
			if(handle.getInstruction() instanceof INVOKEVIRTUAL) {
				target = handle;
				break;
			}
		}

		INVOKEVIRTUAL iv = new INVOKEVIRTUAL(newidx);
		target.setInstruction(iv);


		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		cg.replaceMethod(temp, mg.getMethod());
		cg.getJavaClass().dump("d:/DummyInt.class");
	}
}
