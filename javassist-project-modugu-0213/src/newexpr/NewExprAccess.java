package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static final String TARGET_MY_APP2 = "target.MyAppField";
   static String _L_ = System.lineSeparator();
   static String[] userInputs;

   public static void main(String[] args) throws Throwable {
	   here:
		      while (true) {
		         UtilMenu.showMenuOptions();
		         
				switch (UtilMenu.getOption()) {
		         case 1:
		            System.out.println("Enter 2 inputs (e.g: target.ComponentApp, 1 or target.ServiceApp, 100)");
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
					if(userInputs.length!=2 || error)
		            {
		            	System.out.println("[WRN]Invalid Input");
		            	continue here;
		            }
					 NewExprAccess s = new NewExprAccess();
				     Class<?> c = s.loadClass(userInputs[0]);
				      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
				      mainMethod.invoke(null, new Object[] { args });
		            break;
		         default:
		            break;
		         }
		      }
   }

   private ClassPool pool;

   public NewExprAccess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
            	CtField memberFields[] = newExpr.getEnclosingClass().getDeclaredFields();
               String log = String.format("[Edited by ClassLoader] new expr: %s, " //
                     + "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
                     newExpr.getLineNumber(), newExpr.getSignature());
               System.out.println(log);

               String block1 = "{ " + _L_ //
                     + " $_ = $proceed($$);" + _L_ //
                     + " System.out.print(" + "\"new expr: \" + " //
                     + " $_.getClass().getName() + " + "\", \" );" + _L_; //
					/*
					 * + " System.out.print($_.getClass().getDeclaredFields()[0].getName()" // +
					 * " + \": \" + " + "$_.x + " + "\", \" );" + _L_ // +
					 * " System.out.println($_.getClass().getDeclaredFields()[1].getName()" // +
					 * " + \": \" + " + "$_.y);" + _L_ // // + " System.out.println($type);" + _L_ +
					 * "}";
					 */
               for (int i = 0; i < memberFields.length && i < Integer.parseInt(NewExprAccess.userInputs[1]); i++) {

					String fieldName = memberFields[i].getName();
					block1 = block1 + " System.out.print($_.getClass().getDeclaredFields()[" + i + "].getName()" //
							+ " + \": \" + " + "$_." + fieldName + " );" + _L_;

					if (i + 1 < memberFields.length && i + 1 < Integer.parseInt(NewExprAccess.userInputs[1])) {
						block1 = block1 + " System.out.print(\", \");" + _L_ ;
					} else {
						block1 = block1 + " System.out.println();" + _L_;
					}
				}
               block1 = block1 + "}" + _L_;
               System.out.println(block1);
               newExpr.replace(block1);
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