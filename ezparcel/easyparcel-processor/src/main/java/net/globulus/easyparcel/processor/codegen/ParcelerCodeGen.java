package net.globulus.easyparcel.processor.codegen;

import net.globulus.easyparcel.annotation.EasyParcel;
import net.globulus.easyparcel.processor.ParcelableField;
import net.globulus.easyparcel.processor.util.ProcessorLog;
import net.globulus.easyparcel.processor.util.FrameworkUtil;
import net.globulus.easyparcel.processor.util.TypeUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import javawriter.JavaWriter;

import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_FLAGS;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_PARCEL;
import static net.globulus.easyparcel.processor.util.FrameworkUtil.PARAM_TARGET;

public class ParcelerCodeGen {

    private Filer filer;
    private Elements elementUtils;

    public ParcelerCodeGen(Elements elementUtils, Filer filer) {
        this.filer = filer;
        this.elementUtils = elementUtils;
    }

    public String generate(TypeElement classElement, List<ParcelableField> fields) throws Exception {

        String classSuffix = FrameworkUtil.getParcelerClassExtension();
        String packageName = TypeUtils.getPackageName(elementUtils, classElement);
        String binaryName = TypeUtils.getBinaryName(elementUtils, classElement);

      String originalClassName = classElement.getSimpleName().toString();

        String originFullQualifiedName = classElement.getQualifiedName().toString();
//        Class originalType = Class.forName(originFullQualifiedName);
//		String parcelerClassName = originalClassName + classSuffix;
        String className;
        if (packageName.length() > 0) {
            className = binaryName.substring(packageName.length() + 1) + classSuffix;
        } else {
            className = binaryName + classSuffix;
        }
        String qualifiedName = binaryName + classSuffix;

//		Class parcelType = Class.forName("android.os.Parcel");

//		MethodSpec createFromParcel = MethodSpec
//				.methodBuilder("createFromParcel")
//				.addModifiers(Modifier.PUBLIC)
//				.returns(originalType)
//				.addParameter(parcelType, "in")
//				.addStatement("return new " + originalClassName + "(in)")
//				.build();
//
//		MethodSpec newArray = MethodSpec
//				.methodBuilder("newArray")
//				.addModifiers(Modifier.PUBLIC)
//				.returns(Class.forName("[L" + originalClassName + ";"))
//				.addParameter(int.class, "size")
//				.addStatement("return new " + originalClassName + "[size]")
//				.build();
//
		String creatorClassName = originalClassName + "Creator";
//	 TypeSpec creatorSpec = TypeSpec
//			 .classBuilder(creatorClassName)
//			 .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
//			 .superclass(Class.forName(parcelClassName + ".Creator<" + originalClassName + ">"))
//			 .addMethod(createFromParcel)
//			 .addMethod(newArray)
//			 .build();
//
//      MethodSpec.Builder writeToParcelBuilder = MethodSpec
//			  .methodBuilder("writeToParcel")
//			  .addModifiers(Modifier.PUBLIC)
//			  .returns(void.class)
//			  .addParameter(originalType, "source")
//			  .addParameter(parcelType, "dest");
//
//		MethodSpec.Builder readFromParcelBuilder = MethodSpec
//				.methodBuilder("readFromParcel")
//				.addModifiers(Modifier.PUBLIC)
//				.returns(void.class)
//				.addParameter(originalType, "source")
//				.addParameter(parcelType, "in");
//
//		MethodSpec getCreator = MethodSpec
//				.methodBuilder("getCreator")
//				.addModifiers(Modifier.PUBLIC)
//				.returns(Class.forName(creatorClassName))
//				.addStatement("return new " + creatorClassName + "()")
//				.build();

        JavaFileObject jfo = filer.createSourceFile(qualifiedName, classElement);
		Writer writer = jfo.openWriter();
		JavaWriter jw = new JavaWriter(writer);
//        MethodSpec.Builder builder = new JavaWriter(writer);
//
		jw.emitPackage(packageName);
		jw.emitImports(FrameworkUtil.PARCEL_QUALIFIED_NAME);
		jw.emitImports("android.os.Parcelable");
		jw.emitImports("android.os.Parcelable.Creator");
		jw.emitImports(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelerClassName()));
		jw.emitImports(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelablesClassName()));
		jw.emitImports(originFullQualifiedName);
		jw.emitEmptyLine();
		jw.emitJavadoc("Generated class by @%s . Do not modify this code!",
                EasyParcel.class.getSimpleName());
		jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null,
				FrameworkUtil.getParcelerClassName() + "<" + originalClassName + ">");
		jw.emitEmptyLine();
//


//        generateWriteToParcel(writeToParcelBuilder, originFullQualifiedName, fields);
        generateWriteToParcel(jw, originFullQualifiedName, fields);
//        generateReadFromParcel(readFromParcelBuilder, originFullQualifiedName, fields);
		jw.emitEmptyLine();
        generateReadFromParcel(jw, originFullQualifiedName, fields);

		boolean isAbstract = classElement.getModifiers().contains(Modifier.ABSTRACT);
		if (!isAbstract) {
			jw.beginType(creatorClassName, "class", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC),
					null, "Parcelable.Creator<" + originalClassName + ">");
			jw.beginMethod(originalClassName, "createFromParcel", EnumSet.of(Modifier.PUBLIC), "Parcel", "in");
			jw.emitStatement(originalClassName + " object =  new " + originalClassName + "()");
			jw.emitStatement("Parcelables.readFromParcel(object, in)");
			jw.emitStatement("return object");
			jw.endMethod();
			jw.beginMethod(originalClassName + "[]", "newArray", EnumSet.of(Modifier.PUBLIC), "int", "size");
			jw.emitStatement("return new " + originalClassName + "[size]");
			jw.endMethod();
			jw.endType();

			jw.emitField(creatorClassName, "CREATOR", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL),
					"new " + creatorClassName + "()");
		}
		jw.beginMethod("Parcelable.Creator<" + originalClassName + ">", "getCreator", EnumSet.of(Modifier.PUBLIC));
		if (isAbstract) {
			jw.emitStatement("return null");
		} else {
			jw.emitStatement("return CREATOR");
		}
		jw.endMethod();

		jw.endType();
		jw.close();

//		TypeSpec parcelerSpec = TypeSpec
//				.classBuilder(parcelerClassName)
//				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//				.addSuperinterface(Class.forName("Parceler<" + originalClassName + ">"))
//				.addType(creatorSpec)
//				.addMethod(writeToParcelBuilder.build())
//				.addMethod(readFromParcelBuilder.build())
//				.addMethod(getCreator)
//				.build();

//		JavaFile.Builder javaFile = JavaFile
//				.builder(packageName, parcelerSpec);
//		String string = javaFile.toString();

//		Writer srcWriter = filer.createSourceFile(parcelerClassName).openWriter();
//		javaFile.writeTo(srcWriter);

		return packageName + "." + className;
    }

    /**
     * Generate the writeToParcel method
     *
     * @throws IOException
     */
    private void generateWriteToParcel(JavaWriter jw, String origin,
            List<ParcelableField> fields) throws IOException {
		jw.beginMethod("void", "writeToParcel", EnumSet.of(Modifier.PUBLIC), origin,
				FrameworkUtil.PARAM_SOURCE, "Parcel", FrameworkUtil.PARAM_PARCEL, "int", PARAM_FLAGS);

		for (ParcelableField field : fields) {
			FieldCodeGen gen = field.getCodeGenerator();

			if (gen == null) { // Already checked before, but let's check it again
				ProcessorLog.error(field.getElement(),
						"The field %s is not Parcelable or of unsupported type",
						field.getmFieldName());

				throw new IOException("Unparcelable Field " + field.getmFieldName());
			}

			jw.emitEmptyLine();
			gen.generateWriteToParcel(field, jw);
		}

		jw.endMethod();

    }

    private void generateReadFromParcel(JavaWriter jw, String originClass,
            List<ParcelableField> fields) throws IOException {

		jw.beginMethod("void", "readFromParcel", EnumSet.of(Modifier.PUBLIC),
				originClass, PARAM_TARGET, "Parcel", PARAM_PARCEL);

		for (ParcelableField field : fields) {
			FieldCodeGen gen = field.getCodeGenerator();

			if (gen == null) { // Already checked before, but let's check it again
				ProcessorLog.error(field.getElement(),
						"The field %s is not Parcelable or of unsupported type",
						field.getmFieldName());

				throw new IOException("Unparcelable Field " + field.getmFieldName());
			}

			jw.emitEmptyLine();
			gen.generateReadFromParcel(field, jw);
		}

		jw.endMethod();
    }
}
