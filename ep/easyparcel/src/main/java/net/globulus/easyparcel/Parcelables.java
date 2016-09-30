package net.globulus.easyparcel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by gordanglavas on 29/09/16.
 */
public final class Parcelables {

	private static ParcelerList sParcelerList;

	private Parcelables() { }

	static void setParcelerList(ParcelerList parcelerList) {
		sParcelerList = parcelerList;
	}

	@Nullable
	private static Parceler getParcelerForClass(@NonNull Class clazz) {
		if (sParcelerList == null) {
			try {
				Class c = Class.forName("net.globulus.easyparcel.EasyParcelUtil");
			} catch (ClassNotFoundException e) {
				throw new AssertionError(e);  // Can't happen
			}
		}
		return sParcelerList.getParcelerForClass(clazz);
	}

	public static <T extends Parcelable> void addToParcel(@NonNull T object, @NonNull Parcel dest) {
		Class<?> clazz = object.getClass();
		do {
			Parceler parceler = getParcelerForClass(clazz);
			if (parceler != null) {
				parceler.writeToParcel(object, dest);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

	public static <T extends Parcelable> void readFromParcel(@NonNull T object, @NonNull Parcel in) {
		Class<?> clazz = object.getClass();
		do {
			Parceler parceler = getParcelerForClass(clazz);
			if (parceler != null) {
				parceler.readFromParcel(object, in);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

	@NonNull
	public static <T extends Parcelable> Parcelable.Creator<T> getCreator(@NonNull Class<T> clazz) {
		Parceler<T> parceler = getParcelerForClass(clazz);
		return parceler.getCreator();
	}
}