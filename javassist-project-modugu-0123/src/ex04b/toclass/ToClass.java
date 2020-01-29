package ex04b.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.CommonComponentB;
import target.CommonServiceA;
import util.UtilMenu;

public class ToClass {


   public static void main(String[] args) {
	  here:
      while (true) {
         UtilMenu.showMenuOptions();
         switch (UtilMenu.getOption()) {
         case 1:
            System.out.println("Enter an input:e.g., target.CommonServiceA or target.CommonComponentB");
            String[] inputs = UtilMenu.getArguments();
            if(inputs.length!=1)
            {
            	System.out.println("[WRN]Invalid Input");
            	continue here;
            }
            process(inputs[0]);
            break;
         default:
            break;
         }
      }
      
   }
   
   

   static void process(String clazz) {
      try {
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get(clazz);

         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         String block1 = 
               "System.out.println(\"id: \" + id );";
         System.out.println("[DBG] BLOCK1: " + block1);
         declaredConstructor.insertAfter(block1);

         String block2 = 
                 "System.out.println(\"year: \" + year);";
         System.out.println("[DBG] BLOCK2: " + block2);
         declaredConstructor.insertAfter(block2);


         Class<?> c = cc.toClass();
       if(clazz.equals("target.CommonServiceA"))
       {
         try {
        	 
			CommonServiceA A = (CommonServiceA) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
       else if(clazz.equals("target.CommonComponentB"))
       {
         try {
        	 
			CommonComponentB A = (CommonComponentB) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
      } catch (NotFoundException | CannotCompileException e) {
         System.out.println(e.toString());
      }
      System.exit(0);
   }
}
