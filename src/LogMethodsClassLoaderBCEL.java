import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.util.ClassLoader;

public class LogMethodsClassLoaderBCEL extends ClassLoader {
	//index into the constantpool to System.out
	private static int out;
	//index into the constantpool to PrintStream.println
	private static int println;

	@Override
	protected JavaClass modifyClass(JavaClass clazz) {
		//Get the constantpool of the currently loaded class
		ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());
		//Add the references to System.out and PrintStream.println
		LogMethodsClassLoaderBCEL.out = cp.addFieldref("java.lang.System", "out",
		"Ljava/io/PrintStream;");
		LogMethodsClassLoaderBCEL.println = cp.addMethodref("java.io.PrintStream", "println",
		"(Ljava/lang/String;)V");
		//Go through each method of the class
		Method methods[] = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			//Create the message
			String name = methods[i].getName();
			int flags = methods[i].getAccessFlags();
			String mesg = "BCEL: Method: ";
			mesg+= Utility.methodSignatureToString(
					methods[i].getSignature(), name,
					Utility.accessToString(flags));
			mesg+= " from Class " +clazz.getClassName() +" called";
			//Create the instructions (System.out.println(mesg);)
			InstructionList logPatch = new InstructionList();
			logPatch.append(new GETSTATIC(LogMethodsClassLoaderBCEL.out));
			logPatch.append(new PUSH(cp, mesg));
			logPatch.append(new INVOKEVIRTUAL(LogMethodsClassLoaderBCEL.println));
			//Get the bytecode of the current method
			MethodGen mg =
				new MethodGen(methods[i], clazz.getClassName(), cp);
			InstructionList il = mg.getInstructionList();
			if(il==null) {
				break;
			}
			InstructionHandle[] ihs = il.getInstructionHandles();
			//Check if method is constructor
			if (name.equals("<init>")) {
				// First let the super or other constructor be called
				for (int j = 1; j < ihs.length; j++) {
					if (ihs[j].getInstruction() instanceof INVOKESPECIAL) {
						//Append log instructions
						il.append(ihs[j], logPatch);
						break;
					}
				}
			}
			else {
				//Append log instructions
				il.insert(ihs[0], logPatch);
			}
			//Check if method has enough stack for the System.out.println call
			if (methods[i].getCode().getMaxStack() < 2){
				mg.setMaxStack(2);
			}
			//Set the method to the modified method.
			methods[i] = mg.getMethod();
			il.dispose();
		}
		//Set the constantpool to the changed constantpool
		clazz.setConstantPool(cp.getFinalConstantPool());
		//return the alterd class which now get loaded
		return clazz;
	}
}