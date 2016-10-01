package net.globulus.easyparcel;

import android.os.Parcelable;

/**
 * Created by gordanglavas on 30/09/16.
 */
public interface ParcelerList {

	<T extends Parcelable> Parceler<T> getParcelerForClass(Class<T> clazz);
}
