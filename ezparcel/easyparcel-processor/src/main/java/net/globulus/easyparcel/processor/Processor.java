package net.globulus.easyparcel.processor;

import net.globulus.easyparcel.annotation.EasyParcel;
import net.globulus.easyparcel.annotation.Include;
import net.globulus.easyparcel.processor.codegen.ParcelerCodeGen;
import net.globulus.easyparcel.processor.codegen.ParcelerListCodeGen;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
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

public class Processor extends AbstractProcessor {

	private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
			EasyParcel.class,
			Include.class
	);

	private Elements mElementUtils;
	private Types mTypeUtils;
	private Filer mFiler;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);

		ProcessorLog.init(env);

		mElementUtils = env.getElementUtils();
		mTypeUtils = env.getTypeUtils();
		mFiler = env.getFiler();
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
		ParcelerCodeGen parcelerCodeGen = new ParcelerCodeGen(mElementUtils, mFiler);
		Element lastElement = null;
		for (Element element : roundEnv.getElementsAnnotatedWith(EasyParcel.class)) {
			if (!isValid(element)) {
				continue;
			}

			boolean isAbstract = element.getModifiers().contains(Modifier.ABSTRACT);
			List<ParcelableField> fields = new ArrayList<>();
			lastElement = element;

			EasyParcel annotation = element.getAnnotation(EasyParcel.class);
			boolean autoInclude = annotation.autoInclude();
			Modifier[] ignoreModifiers = annotation.ignoreModifiers();
//			boolean ignoreSuperclass = annotation.ignoreSuperclass(); TODO

			List<? extends Element> memberFields = mElementUtils.getAllMembers((TypeElement) element);

			if (memberFields != null) {
				for (Element member : memberFields) {
					if (member.getKind() != ElementKind.FIELD || !(member instanceof VariableElement)) {
						continue;
					}

					if (!autoInclude) {
						Include fieldAnnotated = member.getAnnotation(Include.class);
						if (fieldAnnotated == null) {
							continue;
						}
					}

					Set<Modifier> modifiers = member.getModifiers();
					if (modifiers.contains(Modifier.STATIC)) {
						continue;
					}

					boolean ignoreField = false;
					for (Modifier modifier : ignoreModifiers) {
						if (modifiers.contains(modifier)) {
							ProcessorLog.note(member, "Ignoring field %s because it is %s",
									member.getSimpleName(), modifier.toString());
							ignoreField = true;
							break;
						}
					}
					if (ignoreField) {
						continue;
					}

					if (modifiers.contains(Modifier.FINAL)) {
						ProcessorLog.error(member,
								"The field %s in %s is final. Final can not be Parcelable", element.getSimpleName(),
								member.getSimpleName());
					}

					fields.add(new ParcelableField((VariableElement) member, mElementUtils, mTypeUtils));
				}
			}

			try {
				TypeElement classElement = (TypeElement) element;
				String name = parcelerCodeGen.generate(classElement, fields);
				annotatedClasses.add(classElement.getQualifiedName().toString());
				parcelerNames.add(name);
			} catch (Exception e) {
				e.printStackTrace();
			}

			new ParcelerListCodeGen().generate(mFiler, lastElement, annotatedClasses, parcelerNames);
		}

		return true;
	}

	private boolean isValid(Element element) {
		if (element.getKind() == ElementKind.CLASS) {
//			if (element.getModifiers().contains(Modifier.ABSTRACT)) {
//				ProcessorLog.error(element,
//						"Element %s is annotated with @%s but is an abstract class. "
//								+ "Abstract classes can not be annotated. Annotate the concrete class "
//								+ "that implements all abstract methods with @%s", element.getSimpleName(),
//						EasyParcel.class.getSimpleName(), EasyParcel.class.getSimpleName());
//				return false;
//			}

			if (element.getModifiers().contains(Modifier.PRIVATE)) {
				ProcessorLog.error(element, "The private class %s is annotated with @%s. "
								+ "Private classes are not supported because of lacking visibility.",
						element.getSimpleName(), EasyParcel.class.getSimpleName());
				return false;
			}

			return true;
		} else {
			ProcessorLog.error(element,
					"Element %s is annotated with @%s but is not a class. Only Classes are supported",
					element.getSimpleName(), EasyParcel.class.getSimpleName());
			return false;
		}
	}
}
