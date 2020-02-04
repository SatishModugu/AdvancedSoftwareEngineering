package ex05.javassistloader;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;


public class SetSuperclass {
   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";
   static ArrayList<String> ComClassNames = new ArrayList<String>();
   static ArrayList<String> classNames = new ArrayList<String>();
   static ArrayList<String> subClassNames = new ArrayList<String>();
   
   public static void setClass(String s1, String s2, String s3 ) throws NotFoundException, CannotCompileException, IOException
   {
	   classNames.add(s1);
	   classNames.add(s2);
	   classNames.add(s3);
	   
	   for(int i=0; i<classNames.size();i++)
	   {
		   String temp = classNames.get(i);
		   if(temp.startsWith("Common"))
		   {
			   ComClassNames.add(temp);
		   }
		   else
		   {
		   subClassNames.add(temp);
		   }
	   }
	   if(ComClassNames.size()==0)
	   {
		   String superc = classNames.get(0);
		   subClassNames.remove(0);
		   setSubClass(superc);   
	   }
	   
	   else if(ComClassNames.size()==1)
	   {
		   String superc = ComClassNames.get(0);
		   setSubClass(superc);
	   }
	   else
	   {
		   int largest=0;
				 for(int i=0; i<ComClassNames.size();i++)
				 {
					 int temp = ComClassNames.get(i).length();
					 if(temp>largest)
					 { 
						 largest=i;
					 }
				 }
				 for(int i=0; i<ComClassNames.size();i++)
				 {
					 if(i!=largest)
					 subClassNames.add(ComClassNames.get(i));
				 }
		 String superc = ComClassNames.get(largest);
		 setSubClass(superc);
	   }
	   
   }
   
   public static void setSubClass(String SuperClass) throws NotFoundException, CannotCompileException, IOException
   {
	   ClassPool pool = ClassPool.getDefault();
	   for(int i=0 ; i<subClassNames.size(); i++)
	   {
		      CtClass cc = pool.get(subClassNames.get(i).trim());
		         setSuperclass(cc, SuperClass, pool);
		         cc.writeFile(outputDir);
		         System.out.println("[DBG] write output to: " + outputDir);
	   }
   }



   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = workDir + SEP + "classfiles"; // eclipse compile dir
      // String strClassPath = workDir + SEP + "classfiles"; // separate dir
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   }
}
