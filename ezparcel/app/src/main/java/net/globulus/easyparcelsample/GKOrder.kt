package net.globulus.easyparcelsample

import android.os.Parcel
import android.os.Parcelable
import net.globulus.easyparcel.EasyParcelable
import net.globulus.easyparcel.annotation.EasyParcel

@EasyParcel
class GKOrder() : EasyParcelable() {

  @JvmField
  var trackingNumbers: Array<String>? = null

  @JvmField
  var trackingLinks: Array<String>? = null

  @JvmField
  var userDismissed = false

  companion object CREATOR : Parcelable.Creator<GKOrder> {
    override fun createFromParcel(parcel: Parcel): GKOrder {
      return GKOrder()
    }

    override fun newArray(size: Int): Array<GKOrder?> {
      return arrayOfNulls(size)
    }
  }
}