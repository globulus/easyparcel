package net.globulus.easyparcel.processor.util;

public final class FrameworkUtil {

	public static String PARCEL_QUALIFIED_NAME = "android.os.Parcel";

	public static final String PARAM_TARGET = "target";
	public static final String PARAM_SOURCE = "source";
	public static final String PARAM_FLAGS = "flags";
	public static final String PARAM_PARCEL = "parcel";

	private FrameworkUtil() { }

	public static String getEasyParcelPackageName() {
		return "net.globulus.easyparcel";
	}

	public static String getParcelablesClassName() {
		return "Parcelables";
	}

	public static String getParcelerClassName() {
		return "Parceler";
	}

	public static String getParcelerClassExtension() {
		return "_EasyParcel" + getParcelerClassName();
	}

	public static String getParcelerListClassName() {
		return "ParcelerList";
	}

	public static String getParcelerListImplClassName() {
		return "EasyParcel" + getParcelerListClassName();
	}

	public static String getQualifiedName(String className) {
		return String.format("%s.%s", getEasyParcelPackageName(), className);
	}
}
