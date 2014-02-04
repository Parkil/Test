package test.bcel;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.MethodGen;

/*
 * DummyClass�� bytecode�� �����ϴ� ����
 */
public class BCELClassGenDummy {
	public static String getLDCValue(Instruction is, ConstantPoolGen cpg) {
		String val = (String)((LDC)is).getValue(cpg);
		return val;
	}

	public static void main(String[] args) throws Exception{
		//1.Class Loading
		JavaClass jc = Repository.lookupClass("test.bcel.dummy.DummyClass");
		ClassGen cg = new ClassGen(jc);

		Method build = null;

		//2.��� Method �˻�
		for(Method m : cg.getMethods()) {
			if(m.getName().intern() == "buildGUI".intern()) {
				build = m;
				System.out.println("Find BuildGUI");
				break;
			}
		}

		//3.ByteCode�� ������ �ӽ� method ����
		ConstantPoolGen cpg	 = cg.getConstantPool(); //Ŭ������ ����� ������ Pool
		MethodGen 		mg	 = new MethodGen(build, cg.getClassName(), cpg);
		InstructionList	list = mg.getInstructionList();

		for(int i = 1 ; i<cpg.getSize() ; i++) {
			/*
			 * Constant Tag
			 * 7  : Class
			 * 8  : String
			 * 1  : utf-8(���� Class�� String�� ���γ����� ǥ�õȴ�.)
			 * 9  : FieldRef
			 * 10 : MethodRef
			 * 12 : NameAndType
			 * 
			 */
			System.out.println("Constant Tag : "+cpg.getConstant(i).getTag()+" Constant : "+cpg.getConstant(i));
		}

		/*
		for(int i = 0 ;i < cpg.getSize() ; i++) {
			System.out.println("constant : "+cpg.getConstant(i));
		}*/

		InstructionHandle titleLDC = null;

		//4.ByteCode ��ɾ�� �����ϰ��� �ϴ� ��ɾ� �˻�(��ɾ� Set�� �̿��Ͽ� �˻�)
		InstructionHandle[] handles = list.getInstructionHandles();

		for(InstructionHandle ih : handles) {
			if(ih.getInstruction() instanceof LDC) {
				titleLDC = ih;

				System.out.println("��ȭȭȭ : "+BCELClassGenDummy.getLDCValue(titleLDC.getInstruction(), cpg)); //LDC�� ���� �� ǥ��

				System.out.println("title LDC found");
				break;
			}
		}

		for(InstructionHandle ih : handles) {
			if(ih.getInstruction() instanceof INVOKESPECIAL) {
				System.out.println("Load Class Type : "+((INVOKESPECIAL)ih.getInstruction()).getLoadClassType(cpg));
			}
		}

		//4-1.ConstantPool���� �����ϰ����ϴ� ���(Constant)�� �˻��ؼ� �����ϴ� ���
		int utf8StringPointer = cpg.lookupUtf8("Moose");
		if(utf8StringPointer != -1) {
			cpg.setConstant(utf8StringPointer, new ConstantUtf8("Spoon"));
			cpg.setConstant(cpg.lookupString("Moose"), new ConstantString(utf8StringPointer));
			System.out.println("Found Moose in Constant Pool");
		}


		//5.��ɾ ���� �����ϰ� ������ ������� ��ɾ ����
		LDC ldc = new LDC(cpg.addString("spoon"));
		titleLDC.setInstruction(ldc);

		//6.��ü ��ɾ �ӽ� method�� �Է�
		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		//7.���� method�� ���θ��� method�� ��ü
		cg.replaceMethod(build, mg.getMethod());

		//8.������ class������ ����
		cg.getJavaClass().dump("d:/DummyClass.class");
	}
}
