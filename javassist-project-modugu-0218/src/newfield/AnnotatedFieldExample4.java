package newfield;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;
import util.UtilMenu;

public class AnnotatedFieldExample4 {
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";
	static ClassPool pool;

	public static void main(String[] args) {
		here: while (true) {
			UtilMenu.showMenuOptions();
			switch (UtilMenu.getOption()) {
			case 1:
				System.out.println("Enter Calss name and two field annotations:(eg,ComponentApp, Column, Author or ServiceApp, Row, Author)");
				String[] inputs = UtilMenu.getArguments();
				if (inputs.length != 3) {
					System.out.println("[WRN]Invalid Input");
					continue here;
				}
				process(inputs[0].trim(), inputs[1].trim(), inputs[2].trim());
				break;
			default:
				break;
			}
		}

	}

	static void process(String className, String annotation1, String annotation2) {
		try {
			pool = ClassPool.getDefault();
			pool.insertClassPath(inputDir);

			CtClass ct = pool.get("target." + className);
			CtField[] cf = ct.getFields();
			for (int i = 0; i < cf.length; i++) {
				ArrayList<String> Annotations = new ArrayList<String>();
				for (int j = 0; j < cf[i].getAnnotations().length; j++) {
					Annotation annotation = getAnnotation(cf[i].getAnnotations()[j]);
					Annotations.add(annotation.getTypeName());
				}
				if (Annotations.contains("target."+annotation1) && Annotations.contains("target."+annotation2)) {
					showAnnotation(getAnnotation(cf[i].getAnnotations()[Annotations.indexOf("target."+annotation2)]));
				}
				
			}

			// process(ct.getAnnotations());
			// System.out.println();

			// process(cf.getAnnotations());
		} catch (NotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	static Annotation getAnnotation(Object obj) {
		// Get the underlying type of a proxy object in java
		AnnotationImpl annotationImpl = //
				(AnnotationImpl) Proxy.getInvocationHandler(obj);
		return annotationImpl.getAnnotation();
	}

	static void showAnnotation(Annotation annotation) {
		Iterator<?> iterator = annotation.getMemberNames().iterator();
		while (iterator.hasNext()) {
			Object keyObj = (Object) iterator.next();
			MemberValue value = annotation.getMemberValue(keyObj.toString());
			System.out.println("[DBG] " + keyObj + ": " + value);
		}

	}
}
