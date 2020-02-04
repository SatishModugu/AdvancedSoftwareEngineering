package ex05.javassistloader;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class JavassistLoaderExample3 {
	private static final String WORK_DIR = System.getProperty("user.dir");
	private static final String TARGET_POINT = "target.Point";
	private static final String TARGET_RECTANGLE = "target.Rectangle";
	static ArrayList<String> checkMethod = new ArrayList<String>();
	public static void main(String[] args) {
		
		while (true) {
			
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			here:
			switch (option) {
			case 1:
				System.out.println("[DBG] Enter (1) a usage method and\n" //
						+ "(2) An increment method and \n(3) a method call to be inserted (e.g., add,incX,getX)");
				String[] arguments = UtilMenu.getArguments();
				if(arguments.length!=3)
				{
					System.out.println("[WRN] Invalid input size!!");
					break here;
				}
				if(checkMethod.contains(arguments[0]))
				{
					System.out.println("[WRN] This method 'method name' has beeen modified!!");
					break here;
				}
				else
				{
				analysisProcess(arguments[0], arguments[1],  arguments[2]);
				}
				break;
			default:
				break;
			}
		}
	}

	static void analysisProcess(String methodDecl, String methodCall, String getterMethod) {
		try {
			
			System.out.println("Enter threre classes:(eg: target.Point,target.Rectangle,target.Circle)");
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			String[] classes = input.split(",");
			if (classes.length != 3) {
				System.out.println("[ERR] Terminating the project as the number of inputs is not equal to 3");
				System.exit(0);
			} else {
				SetSuperclass.setClass(classes[0], classes[1], classes[2]);
			}
			ClassPool cp = ClassPool.getDefault();
			insertClassPath(cp);
			CtClass cc = cp.get(TARGET_RECTANGLE);
			cc.defrost();
			//cc.setSuperclass(cp.get(TARGET_POINT));
			CtMethod m1 = cc.getDeclaredMethod(methodDecl);
			String BLK1 = "\n{\n" //
					+ "\t" + methodCall + "();" + "\n" //
					+ "\t" + "System.out.println(\"[TR] result : \" +"+ getterMethod +"()); " + "\n" + "}";
			System.out.println("[DBG] Block: " + BLK1);
			m1.insertBefore(BLK1);

			Loader cl = new Loader(cp);
			Class<?> c = cl.loadClass(TARGET_RECTANGLE);
			Object rect = c.newInstance();
			System.out.println("[DBG] Created a Rectangle object.");

			Class<?> rectClass = rect.getClass();
			Method m = rectClass.getDeclaredMethod("add", new Class[] {});
			System.out.println("[DBG] Called getDeclaredMethod.");
			Object invoker = m.invoke(rect, new Object[] {});
			System.out.println("[DBG] getVal result: " + invoker);
			checkMethod.add(methodDecl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = WORK_DIR + File.separator + "classfiles";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}
}
