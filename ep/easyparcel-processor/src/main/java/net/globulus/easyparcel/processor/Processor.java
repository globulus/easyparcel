package net.globulus.easyparcel.processor;

import com.squareup.javawriter.JavaWriter;

import net.globulus.easyparcel.annotation.Add;
import net.globulus.easyparcel.annotation.EasyParcel;
import net.globulus.easyparcel.processor.codegenerator.CodeGenerator;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class Processor extends AbstractProcessor {

	private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
			EasyParcel.class,
			Add.class
	);

	private Elements elementUtils;
	private Types typeUtils;
	private Filer filer;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);

		ProcessorLog.init(env);

//		String sdk = env.getOptions().get(OPTION_SDK_INT);
//		if (sdk != null) {
//			try {
//				this.sdk = Integer.parseInt(sdk);
//			} catch (NumberFormatException e) {
//				env.getMessager()
//						.printMessage(Diagnostic.Kind.WARNING, "Unable to parse supplied minSdk option '"
//								+ sdk
//								+ "'. Falling back to API 1 support.");
//			}
//		}

		elementUtils = env.getElementUtils();
		typeUtils = env.getTypeUtils();
		filer = env.getFiler();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new LinkedHashSet<>();
		for (Class<? extends Annotation> annotation : ANNOTATIONS) {
			types.add(annotation.getCanonicalName());
		}
		return types;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		List<String> annotatedClasses = new ArrayList<>();
		List<String> parcelerNames = new ArrayList<>();
		CodeGenerator codeGenerator = new CodeGenerator(elementUtils, filer);
		Element lastElement = null;
		for (Element element : roundEnv.getElementsAnnotatedWith(EasyParcel.class)) {
			if (!isValid(element)) {
				continue;
			}

			List<ParcelableField> fields = new ArrayList<>();

			lastElement = element;

//			ParcelablePlease annotation = element.getAnnotation(ParcelablePlease.class);
//			boolean allFields = annotation.allFields();
//			boolean ignorePrivateFields = annotation.ignorePrivateFields();

			List<? extends Element> memberFields = elementUtils.getAllMembers((TypeElement) element);

			if (memberFields != null) {
				for (Element member : memberFields) {
					// Search for fields

					if (member.getKind() != ElementKind.FIELD || !(member instanceof VariableElement)) {
						continue; // Not a field, so go on
					}

					// it's a field, so go on

//					ParcelableNoThanks skipFieldAnnotation = member.getAnnotation(ParcelableNoThanks.class);
//					if (skipFieldAnnotation != null) {
//						// Field is marked as not parcelabel, so continue with the next
//						continue;
//					}

//					if (!allFields) {
//						ParcelableThisPlease fieldAnnotated = member.getAnnotation(ParcelableThisPlease.class);
//						if (fieldAnnotated == null) {
//							// Not all fields should parcelable,
//							// and this field is not annotated as parcelable, so skip this field
//							continue;
//						}
//					}

					// Check the visibility of the field and modifiers
					Set<Modifier> modifiers = member.getModifiers();

					if (modifiers.contains(Modifier.STATIC)) {
						// Static fields are skipped
						continue;
					}

//					if (modifiers.contains(Modifier.PRIVATE)) {
//
////						if (ignorePrivateFields) {
////							continue;
////						}
//
//						ProcessorLog.error(member,
//								"The field %s  in %s is private. At least default package visibility is required "
//										+ "or annotate this field as not been parcelable with @%s "
//										+ "or configure this class to ignore private fields "
//										+ "with @%s( ignorePrivateFields = true )", member.getSimpleName(),
//								element.getSimpleName(), ParcelableNoThanks.class.getSimpleName(),
//								ParcelablePlease.class.getSimpleName());
//					}

					if (modifiers.contains(Modifier.FINAL)) {
						ProcessorLog.error(member,
								"The field %s in %s is final. Final can not be Parcelable", element.getSimpleName(),
								member.getSimpleName());
					}

					// If we are here the field is be parcelable
					fields.add(new ParcelableField((VariableElement) member, elementUtils, typeUtils));
				}
			}

			//
			// Generate the code
			//
			try {
				TypeElement classElement = (TypeElement) element;
				String name = codeGenerator.generate(classElement, fields);
				annotatedClasses.add(classElement.getQualifiedName().toString());
				parcelerNames.add(name);
			} catch (Exception e) {
				e.printStackTrace();
//				ProcessorMessage.error(lastElement, "An error has occurred while processing %s : %s",
//						element.getSimpleName(), e.getMessage());
			}
		}

			try {

				String packageName = "net.globulus.easyparcel";
				String className = "EasyParcelUtil";
				String innerClassName = className + "Inner";

				JavaFileObject jfo = filer.createSourceFile(packageName + "." + className, lastElement);
				Writer writer = jfo.openWriter();
				JavaWriter jw = new JavaWriter(writer);
				jw.emitPackage(packageName);
				jw.emitImports("java.util.Map");
				jw.emitImports("java.util.HashMap");
				jw.emitImports("android.os.Parcelable");
				jw.emitImports("net.globulus.easyparcel.Parceler");
				jw.emitImports("net.globulus.easyparcel.Parcelables");
				jw.emitImports("net.globulus.easyparcel.ParcelerList");
				jw.emitEmptyLine();

				jw.emitJavadoc("Generated class by @%s . Do not modify this code!",
						EasyParcel.class.getSimpleName());
				jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
				jw.emitEmptyLine();

				jw.beginType(innerClassName, "class", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC), null, "ParcelerList");
				jw.emitField("Map<Class<? extends Parcelable>, Parceler>", "map");
				jw.beginConstructor(EnumSet.of(Modifier.PUBLIC));
				jw.emitStatement("map = new HashMap<>()");
				for (int i = 0; i < annotatedClasses.size(); i++) {
					jw.emitStatement("map.put(" + annotatedClasses.get(i) + ".class, new " + parcelerNames.get(i) + "())");
				}
				jw.emitStatement("Parcelables.setParcelerList(this)");
				jw.emitStatement("System.out.println(\"aaaaaaa\")");
				jw.endConstructor();

				jw.beginMethod("<T extends Parcelable> Parceler<T>", "getParcelerForClass",
						EnumSet.of(Modifier.PUBLIC), "Class<T>", "clazz");
				jw.emitStatement("return map.get(clazz)");
				jw.endMethod();
				jw.endType();

				jw.emitField(innerClassName, "INSTANCE", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL),
						"new " + innerClassName + "()");


				jw.endType();
				jw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		return true;
	}

	private boolean isValid(Element element) {
		if (element.getKind() == ElementKind.CLASS) {
			if (element.getModifiers().contains(Modifier.ABSTRACT)) {
				ProcessorLog.error(element,
						"Element %s is annotated with @%s but is an abstract class. "
								+ "Abstract classes can not be annotated. Annotate the concrete class "
								+ "that implements all abstract methods with @%s", element.getSimpleName(),
						EasyParcel.class.getSimpleName(), EasyParcel.class.getSimpleName());
				return false;
			}

			if (element.getModifiers().contains(Modifier.PRIVATE)) {
				ProcessorLog.error(element, "The private class %s is annotated with @%s. "
								+ "Private classes are not supported because of lacking visibility.",
						element.getSimpleName(), EasyParcel.class.getSimpleName());
				return false;
			}

			// Ok, its a valid class
			return true;
		} else {
			ProcessorLog.error(element,
					"Element %s is annotated with @%s but is not a class. Only Classes are supported",
					element.getSimpleName(), EasyParcel.class.getSimpleName());
			return false;
		}
	}
}
