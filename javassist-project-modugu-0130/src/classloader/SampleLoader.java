package classloader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String TARGET_APP = "MyApp";
   private ClassPool pool;
   static String[] userInputs = null;
   public static void main(String[] args) throws Throwable {
      SampleLoader loader = new SampleLoader();
		System.out.println("Enter application class name and filed name:(eg: ComponentApp,f1 or ServiceApp,f2) ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		userInputs = input.split(",");
		Arrays.stream(userInputs).map(String::trim).toArray(temp -> userInputs);
      Class<?> c = loader.loadClass(userInputs[0]);
      c.getDeclaredMethod("main", new Class[] { String[].class }). //
            invoke(null, new Object[] { userInputs });
   }

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the byte code.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals(userInputs[0])) {
            CtField f = new CtField(CtClass.doubleType, userInputs[1], cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f, CtField.Initializer.constant(11.1));
         }
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
