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
 * Method�� ���� �����ϰ� ������ �޼ҵ带 ȣ���ϵ��� �����ϴ� ����
 */
public class BCELAddMethod {
	private JavaClass			jc;
	private ClassGen			cg;
	private ConstantPoolGen		cpg;
	private InstructionFactory	fac;

	//Ŭ���������� BCEL�ʱ�ȭ(JavaClass,ClassGen,ConstantPoolGen�� ������ ���ۿ��� �� ���� InstructionFactory�ǰ�� ��ɾ� ������ �����ϰ� ���ִ� Ŭ������
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

	//���ο� method�� �����ϰ� methodGen�� ��ȯ ���߿� cg.addMethod(newmg.getMethod())�������� ������ method�� Ŭ������ �߰���
	MethodGen addMethod() {
		//InstructionList ����
		InstructionList 	il = new InstructionList();

		//InstructionList�� ���� ��ɾ� �Է�
		il.append(fac.createPrintln("Puhahaha")); //InstructionFactory�� �̿��Ͽ� System.out.println��ɾ� ������ �Է�
		il.append(InstructionFactory.createReturn(Type.VOID)); //�̺κ��� ��������  ����� Falling off the end of the code ������ �߻���

		/*
		 * MethodGen - ���ο� �޼ҵ带 ����
		 * 1.method�� public/private���� ���� static�� ��� ������ ���� ó�� Constants.ACC_PUBLIC | Constants.ACC_STATIC
		 * 2.method ����Ÿ��
		 * 3.����Ÿ�� Type[]
		 * 4.�����̸� String[]
		 * 5.method �̸�
		 * 6.Ŭ������
		 * 7.InstructionList
		 * 8.ConstantPoolGen
		 */

		MethodGen newmg = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[]{}, "puhaha", cg.getClassName(), il, cpg);
		newmg.setInstructionList(il); //method�� ��ɾ ����
		newmg.setMaxStack(); //�޼ҵ��� max_stack ���� �ʿ��� stack������ max_stack���� ������  ����� Stack size too large ������ �߻���
		newmg.setMaxLocals(); //�޼ҵ��� max_locals���� (�޼ҵ峻���� ������������)
		newmg.removeLineNumbers(); //line number ���� ���ϴ°����� �𸣰���

		return newmg;
	}

	//���� Mehtod�� �����Ͽ� ������ �ִ� �޼ҵ�ȣ���� �߰�
	MethodGen replaceMethod_addref() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		Type[] arguments = Type.getArgumentTypes("()V"); //�����ڿ� ���� ���ڸ� ����

		ALOAD load = new ALOAD(0); //0�� Ŭ���� ������ �����ϴ� �� - non-static �޼ҵ�ȿ��� non-static �޼ҵ带 ȣ���Ϸ��� ���� �� ������ ȣ���ؾ� ��
		list.insert(list.getEnd(),load);
		list.insert(list.getEnd(), fac.createInvoke("DummyInt", "puhaha", Type.VOID, arguments, Constants.INVOKEVIRTUAL));

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//���� Mehtod�� �����Ͽ� ���������� ����
	MethodGen replaceMethod_addlocalvar() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		/*
		 * ���������� ���� �����ϰ� ������ Ư������ �Է�
		 * 
		 * MethodGen addLocalVariable
		 * 1.���� �̸�
		 * 2.���� Ÿ�� (short,int,long...) �������� ��쿡�� ObjectType�̿�
		 * 3.���� scope(��ȿ����) ����
		 * 4.���� scope(��ȿ����) ��
		 */

		//�������� ����
		LocalVariableGen lvg = mg.addLocalVariable("t5", Type.STRING, list.getStart(), list.getEnd());
		int lvg_idx = lvg.getIndex();


		LDC ldc = new LDC(cpg.addString("1234567")); //������ ������ ���� �Է� short�� ��� SIPUSH,String/int/float�� ��� LDC.. �� ���������� �´� ���� ��ɾ ����
		//SIPUSH ldc = new SIPUSH((short)4000); /
		//ISTORE is = new ISTORE(lvg_idx); //������(int,short)�� ��� ISTORE boolean/byte�� ��� BASTORE... �� ������ ���� �´� �����ɾ ����
		ASTORE is = new ASTORE(lvg_idx); //������(�⺻���� ���� ��ǻ� String�� ����)�� ��쿡�� ASTORE�̿�

		list.insert(list.getEnd(),ldc);
		list.insert(list.getEnd(),is);

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//���� Mehtod�� �����Ͽ� ������ü���� �����ϰ� ��ü ����
	MethodGen replaceMethod_addObjectvar() {
		Method target = getMethod("methodA");

		MethodGen mg = new MethodGen(target, cg.getClassName(), cpg);
		InstructionList list = mg.getInstructionList();

		/*
		 * ���������� ���� �����ϰ� ������ Ư������ �Է�
		 * 
		 * MethodGen addLocalVariable
		 * 1.���� �̸�
		 * 2.���� Ÿ�� (short,int,long...) �������� ��쿡�� ObjectType�̿�
		 * 3.���� scope(��ȿ����) ����
		 * 4.���� scope(��ȿ����) ��
		 */

		//������ü���� ����
		ObjectType ot = new ObjectType("Test5"); //��üŸ���� ����
		LocalVariableGen lvg = mg.addLocalVariable("t5", ot, list.getStart(), list.getEnd()); //�������� ������ type�� ������ ObjectType�� ����
		int lvg_idx = lvg.getIndex();

		/*
		 * InsturctionFactory createInvoke
		 * 1.Ŭ���� �̸�
		 * 2.�޼ҵ��		 (�߸� ������  java.lang.NoSuchMethodError �߻�)
		 * 3.�޼ҵ帮��Ÿ�� (�߸� ������  java.lang.NoSuchMethodError �߻�)
		 * 4.����(Type �迭) ������� Type.NO_ARGS�Է�
		 * 5.ȣ���� (�̰� �߸������ɰ��  Ŭ���� ���� ����� Unable to pop operand off an empty stack ������ �߻��Ѵ� ������ ���� ���)
		 * 	5-1.INVOKESTATIC  : static method ȣ��
		 * 	5-2.INVOKEVIRTUAL : non - static method ȣ��
		 * 	5-3.INVOKESPECIAL : ����Ŭ������ method �� ȣ���ϰų� private �Ǵ� instance �ʱ�ȭ method ȣ��
		 */

		//��ü ���� new Test5();
		Type[] arguments = Type.getArgumentTypes("()V"); //�����ڿ� ���� ���ڸ� ����
		list.insert(list.getEnd(), fac.createNew("Test5"));
		list.insert(list.getEnd(), InstructionConstants.DUP); //������ ���ڰ� ���������� InstructionConstants.DUP ������ ��üŸ�Կ� �´� ���ڸ� �Է� - �̺κ��� �߸��ԷµǸ� Ŭ���� ���� ����� Expecting to find object/array on stack ������ �߻�
		list.insert(list.getEnd(), fac.createInvoke("Test5", "<init>", Type.VOID, arguments, Constants.INVOKESPECIAL));

		ASTORE is = new ASTORE(lvg_idx); //��ü�� �ʱ�ȭ �ؼ� ������ ���������� ���� �������� ��쿡�� ASTORE�̿�
		list.insert(list.getEnd(), is);

		ALOAD load = new ALOAD(lvg_idx); //�ʱ�ȭ�� ��ü�� ������ ���������� load
		list.insert(list.getEnd(), load);
		list.insert(list.getEnd(), fac.createInvoke("Test5", "a", Type.VOID, arguments, Constants.INVOKEVIRTUAL)); //Test5 t5 =

		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		return mg;
	}

	//Ŭ�������� ���ڷ��־��� �̸��� �ش��ϴ� �޼ҵ� ��ü�� ��ȯ
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
