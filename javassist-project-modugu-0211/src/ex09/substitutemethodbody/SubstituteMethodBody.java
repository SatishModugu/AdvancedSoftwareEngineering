package ex09.substitutemethodbody;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

   static final String TARGET_MY_APP = "target.MyApp";
   static final String MOVE_METHOD = "move";
   static final String DRAW_METHOD = "draw";
   static String[] userInputs;
   static ArrayList<String> prevMethod = new ArrayList<String>();

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
	   here:
		      while (true) {
		         UtilMenu.showMenuOptions();
		         switch (UtilMenu.getOption()) {
		         case 1:
		            System.out.println("Enter an input:e.g., (ComponentApp, move, 1,0) or (ServiceApp, fill, 1,0)");
		            userInputs = UtilMenu.getArguments();
		           boolean error = false;
		            Arrays.stream(userInputs).map(String::trim).toArray(temp -> userInputs);
		            for(int i=0;i<userInputs.length;i++)
		            {
		            	if(userInputs[i].isEmpty())
		            	{
		            		error = true;
		            	}
		            }
					if(userInputs.length!=4 || error)
		            {
		            	System.out.println("[WRN]Invalid Input");
		            	continue here;
		            }
					if(prevMethod.contains(userInputs[1]))
					{
						System.out.println("[WRN]This method " + userInputs[1] + " has been modified!");
						continue here;
					}
		            SubstituteMethodBody s = new SubstituteMethodBody();
		            Class<?> c = s.loadClass("target."+userInputs[0]);
		            Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
		            mainMethod.invoke(null, new Object[] { args });
		            prevMethod.add(userInputs[1]);
		            break;
		         default:
		            break;
		         }
		      }
    
   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
      System.out.println("[DBG] Class Pathes: " + pool.toString());
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         if (!cc.getName().equals("target."+userInputs[0])) {
            return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
         }

         cc.instrument(new ExprEditor() {
            public void edit(MethodCall call) throws CannotCompileException {
               String className = call.getClassName();
               String methodName = call.getMethodName();

               if (className.equals("target."+userInputs[0]) && methodName.equals(userInputs[1])) {
                  System.out.println("[Edited by ClassLoader] method name: " + userInputs[1] + ", line: " + call.getLineNumber());
                  String block2 = "{" + _L_ //
                        + "System.out.println(\"\tReset param to zero.\"); " + _L_ //
                        + "$"+userInputs[2]+"=" +userInputs[3]+"; " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK2: " + block2);
                  System.out.println("------------------------");
                  call.replace(block2);
               }
            }
         });
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         e.printStackTrace();
         throw new ClassNotFoundException();
      }
   }
}
