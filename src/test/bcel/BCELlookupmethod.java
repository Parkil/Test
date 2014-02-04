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
 * Method�� index�� ã�� ����
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
		 * lookupMethodref - ConstantPoolGen���� Method index�� ��ȯ�Ѵ� index�� ���߿� INVOKEVIRTUAL ������ ���ڷ� ���� �ִ�.
		 * 					 methodref�� ��� class���� ȣ��� method�� �˻��� �Ѵ�. ȣ����� ���� method�� �˻��Ϸ���
		 * 					 ConstantPoolGen���� cpg.addMethodref(cg.getClassName(), "methodB", "(II)V")�� ȣ���Ͽ��� �Ѵ�.
		 * 					 (Ŭ������, �޼ҵ��, signature) - Method index�� ��ȯ��
		 * 
		 * Parameter
		 * 1.lookupMethodref(MethodGen gen)
		 *   - MethodGen Instance
		 *
		 * 2.lookupMethodref(String classname, String methodname, String signature);
		 *   - classname Ŭ������
		 *   - methodname �޼ҵ��
		 *   - signature �޼ҵ����� �� return type
		 *   =�⺻���� return type=
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

		 *   ex) ���ڰ� ���� void�� ����
		 *   "()V"
		 * 
		 *   ex) ���ڿ� ���ڸ� �ް� void�� ����
		 *   "(Ljava/lang/String;)V
		 * 
		 *   ex) int ���� 2�� void�� ����
		 *   (II)V
		 * 
		 *   �� �𸦰�� ���� ���� ����
		 * 
		 *   Type param[] = {Type.INT,Type.INT};
		 *   //������ - ���� Ÿ�� ������ - �Ķ����
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
