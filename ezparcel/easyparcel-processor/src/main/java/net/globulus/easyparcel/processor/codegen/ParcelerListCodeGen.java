package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.annotation.EasyParcel;
import net.globulus.easyparcel.processor.util.FrameworkUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
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

	public void generate(Filer filer, Input input) {
		try {

			String packageName = FrameworkUtil.getEasyParcelPackageName();
			String className = FrameworkUtil.getParcelerListImplClassName();
			String innerClassName = className + "Inner";

			JavaFileObject jfo = filer.createSourceFile(packageName + "." + className, input.lastElement);
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
			for (int i = 0; i < input.annotatedClasses.size(); i++) {
				jw.emitStatement("map.put(" + input.annotatedClasses.get(i) + ".class, new " + input.parcelerNames.get(i) + "())");
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

	public static class Input implements Serializable {

		public Element lastElement;
		public final List<String> annotatedClasses;
		public final List<String> parcelerNames;

		public Input(Element lastElement, List<String> annotatedClasses, List<String> parcelerNames) {
			this.lastElement = lastElement;
			this.annotatedClasses = annotatedClasses;
			this.parcelerNames = parcelerNames;
		}

		public Input mergedUp(Input other) {
			Element lastElement = (other.lastElement != null) ? other.lastElement : this.lastElement;
			List<String> annotatedClasses = new ArrayList<>(other.annotatedClasses);
			annotatedClasses.addAll(this.annotatedClasses);
			List<String> parcelerNames = new ArrayList<>(other.parcelerNames);
			parcelerNames.addAll(this.parcelerNames);
			return new Input(lastElement, annotatedClasses, parcelerNames);
		}

		public static Input fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
			try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				 ObjectInput in = new ObjectInputStream(bis)) {
				return (Input) in.readObject();
			}
		}
	}
}
