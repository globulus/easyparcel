package net.globulus.easyparcelsample;

import android.os.Parcel;
import android.os.Parcelable;

import net.globulus.easyparcel.Parcelables;
import net.globulus.easyparcel.annotation.EasyParcel;

import java.util.Date;

/**
 * Created by gordanglavas on 30/09/16.
 */
@EasyParcel
public class Goal implements Parcelable {

	public int goalType;

	public Date endDate;

	public int parameter1;

	public int parameter2;

	public Float carbs;

	public Float calories;

	public Float fat;

	public Float protein;

	public String name;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Parcelables.writeToParcel(this, dest, flags);
	}
}
