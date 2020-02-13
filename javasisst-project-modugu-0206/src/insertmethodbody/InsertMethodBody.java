package insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;
import util.UtilStr;

public class InsertMethodBody {
  // private static final String TARGET_MYAPP = null;
static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output" ;

   static String _L_ = System.lineSeparator();
   private ClassPool pool;
   public static void main(String[] args) {
	   here:
		      while (true) {
		         UtilMenu.showMenuOptions();
		         switch (UtilMenu.getOption()) {
		         case 1:
		            System.out.println("Enter an input:e.g., (target.ComponentApp, foo, 1) or (target.ServiceApp, bar, 1)");
		            String[] inputs = UtilMenu.getArguments();
		            if(inputs.length!=3)
		            {
		            	System.out.println("[WRN]Invalid Input");
		            	continue here;
		            }
		            process(inputs[0].trim(),inputs[1].trim(),inputs[2].trim());
		            
		        
		            
		            break;
		         default:
		            break;
		         }
		      }
}

	
   static void process(String clazz, String methodName, String index)
   {
	   try {
		   String ind =""; 
	         ClassPool pool = ClassPool.getDefault();
	         pool.insertClassPath(INPUT_DIR);
	         CtClass cc = pool.get(clazz);
	         cc.defrost();
	         CtMethod m = cc.getDeclaredMethod(methodName);
	         if(index.equals("1"))
	        	 ind="$1";
	         else
	        	 ind ="$2";
	         
	         String block1 = "{ " + _L_ //
	               + "System.out.println(\"[DBG] [inserted]"+clazz+"."+methodName+"'s"+" param "+index+": \"+ " + ind+"); " + _L_ //
	               +
	               "}";
	         System.out.println(block1);
	         m.insertBefore(block1);
	         cc.writeFile(OUTPUT_DIR);
	         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
	         String args[] = null;
	         Loader s = new Loader(pool);
				Class<?> c = s.loadClass(clazz);
				Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
	            mainMethod.invoke(null, new Object[] { args });
	      } catch (NotFoundException | CannotCompileException | IOException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	         e.printStackTrace();
	      }
	   }
   }
