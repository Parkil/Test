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
 * DummyClass의 bytecode를 수정하는 예제
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

		//2.대상 Method 검색
		for(Method m : cg.getMethods()) {
			if(m.getName().intern() == "buildGUI".intern()) {
				build = m;
				System.out.println("Find BuildGUI");
				break;
			}
		}

		//3.ByteCode를 수정할 임시 method 생성
		ConstantPoolGen cpg	 = cg.getConstantPool(); //클래스의 상수를 저장한 Pool
		MethodGen 		mg	 = new MethodGen(build, cg.getClassName(), cpg);
		InstructionList	list = mg.getInstructionList();

		for(int i = 1 ; i<cpg.getSize() ; i++) {
			/*
			 * Constant Tag
			 * 7  : Class
			 * 8  : String
			 * 1  : utf-8(보통 Class나 String의 세부내용이 표시된다.)
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

		//4.ByteCode 명령어에서 수정하고자 하는 명령어 검색(명령어 Set을 이용하여 검색)
		InstructionHandle[] handles = list.getInstructionHandles();

		for(InstructionHandle ih : handles) {
			if(ih.getInstruction() instanceof LDC) {
				titleLDC = ih;

				System.out.println("음화화화 : "+BCELClassGenDummy.getLDCValue(titleLDC.getInstruction(), cpg)); //LDC의 실제 값 표시

				System.out.println("title LDC found");
				break;
			}
		}

		for(InstructionHandle ih : handles) {
			if(ih.getInstruction() instanceof INVOKESPECIAL) {
				System.out.println("Load Class Type : "+((INVOKESPECIAL)ih.getInstruction()).getLoadClassType(cpg));
			}
		}

		//4-1.ConstantPool에서 수정하고자하는 상수(Constant)를 검색해서 수정하는 방법
		int utf8StringPointer = cpg.lookupUtf8("Moose");
		if(utf8StringPointer != -1) {
			cpg.setConstant(utf8StringPointer, new ConstantUtf8("Spoon"));
			cpg.setConstant(cpg.lookupString("Moose"), new ConstantString(utf8StringPointer));
			System.out.println("Found Moose in Constant Pool");
		}


		//5.명령어를 새로 생성하고 덮어씌우는 방식으로 명령어를 수정
		LDC ldc = new LDC(cpg.addString("spoon"));
		titleLDC.setInstruction(ldc);

		//6.전체 명령어를 임시 method에 입력
		list.setPositions();
		mg.setInstructionList(list);
		mg.setMaxStack();
		mg.setMaxLocals();
		mg.removeLineNumbers();

		//7.기존 method를 새로만든 method로 대체
		cg.replaceMethod(build, mg.getMethod());

		//8.수정된 class파일을 저장
		cg.getJavaClass().dump("d:/DummyClass.class");
	}
}
