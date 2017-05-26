package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.annotation.EasyParcel;
import net.globulus.easyparcel.processor.util.FrameworkUtil;

import java.io.Writer;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import javawriter.EzpJavaWriter;

/**
 * Created by gordanglavas on 01/10/16.
 */
public class ParcelerListCodeGen {

	public void generate(Filer filer,
						 Element lastElement,
						 List<String> annotatedClasses,
						 List<String> parcelerNames) {
		try {

			String packageName = FrameworkUtil.getEasyParcelPackageName();
			String className = FrameworkUtil.getParcelerListImplClassName();
			String innerClassName = className + "Inner";

			JavaFileObject jfo = filer.createSourceFile(packageName + "." + className, lastElement);
			Writer writer = jfo.openWriter();
			EzpJavaWriter jw = new EzpJavaWriter(writer);
			jw.emitPackage(packageName);
			jw.emitImports("java.util.Map");
			jw.emitImports("java.util.HashMap");
			jw.emitImports("java.util.Set");
			jw.emitImports("android.os.Parcelable");
			jw.emitImports(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelerClassName()));
			jw.emitImports(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelablesClassName()));
			jw.emitImports(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelerListClassName()));
			jw.emitEmptyLine();

			jw.emitJavadoc("Generated class by @%s . Do not modify this code!",
					EasyParcel.class.getSimpleName());
			jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
			jw.emitEmptyLine();

			jw.beginType(innerClassName, "class", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC),
					null, FrameworkUtil.getParcelerListClassName());
			jw.emitField("Map<Class<? extends Parcelable>, " + FrameworkUtil.getParcelerClassName() + ">", "map");
			jw.beginConstructor(EnumSet.of(Modifier.PUBLIC));
			jw.emitStatement("map = new HashMap<>()");
			for (int i = 0; i < annotatedClasses.size(); i++) {
				jw.emitStatement("map.put(" + annotatedClasses.get(i) + ".class, new " + parcelerNames.get(i) + "())");
			}
			jw.emitStatement(FrameworkUtil.getParcelablesClassName() + ".setParcelerList(this)");
			jw.endConstructor();

			jw.beginMethod("<T extends Parcelable> " + FrameworkUtil.getParcelerClassName() + "<T>",
					"getParcelerForClass",
					EnumSet.of(Modifier.PUBLIC), "Class<T>", "clazz");
			jw.emitStatement("return map.get(clazz)");
			jw.endMethod();

			jw.beginMethod("Set<Class<? extends Parcelable>>", "getAllClasses", EnumSet.of(Modifier.PUBLIC));
			jw.emitStatement("return map.keySet()");
			jw.endMethod();

			jw.endType();

			jw.emitField(innerClassName, "INSTANCE", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL),
					"new " + innerClassName + "()");


			jw.endType();
			jw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
