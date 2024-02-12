package kr.co.lion.android_homeworkproject_two

import android.icu.text.SimpleDateFormat
import android.os.Parcel
import android.os.Parcelable


// Parcelable 인터페이스 구현.
class MemoData(var memoTitle: String?, var memoDetail:String?) :Parcelable{
    // 작성날짜 구하기
    val currentTime : Long = System.currentTimeMillis()
    val dataFormat = SimpleDateFormat("yyyy-MM-dd")
    val memoDate = dataFormat.format(currentTime)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(memoTitle)
        parcel.writeString(memoDetail)
        parcel.writeString(memoDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemoData> {
        override fun createFromParcel(parcel: Parcel): MemoData {
            return MemoData(parcel)
        }

        override fun newArray(size: Int): Array<MemoData?> {
            return arrayOfNulls(size)
        }
    }
}