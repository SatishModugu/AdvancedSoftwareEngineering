package cEx02.defineNewClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class DefineNewClass {
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	   static ArrayList<String> ComClassNames = new ArrayList<String>();
	   static ArrayList<String> classNames = new ArrayList<String>();
	   static ArrayList<String> subClassNames = new ArrayList<String>();
	
	public static void showoption()
	{
		System.out.println("Enter three classes");
	}
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
			      CtClass cc = pool.get(subClassNames.get(i));
			         setSuperclass(cc, SuperClass, pool);
			         cc.writeFile(outputDir);
			         System.out.println("[DBG] write output to: " + outputDir);
		   }
	   }
	   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
		      curClass.setSuperclass(pool.get(superClass));
		      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
		            ", subclass: " + curClass.getName());
		   }
	

	public static void main(String[] args)  {
		here:
			while(true)
		{
		showoption();
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		String[] classes = input.split(",");
		if (classes.length != 3) {
			System.out.println("[WRN] Invalid Input");
			continue here;
		}
		try {
			ClassPool pool = ClassPool.getDefault();
			for(int i=0;i<classes.length;i++)
			{
	         CtClass cc = makeClass(pool, classes[i]);
	         cc.writeFile(outputDir);
	         System.out.println("[DBG] write output to: " + outputDir);
	         cc.defrost();
			}
			setClass(classes[0],classes[1],classes[2]);
			System.exit(0);
		} catch (NotFoundException | CannotCompileException | IOException e) {
			e.printStackTrace();
		}
		}
		}
		

	static CtClass makeClass(ClassPool pool, String newClassName) {
		CtClass cc = pool.makeClass(newClassName);
		System.out.println("[DBG] make class: " + cc.getName());
		return cc;
	}
}
