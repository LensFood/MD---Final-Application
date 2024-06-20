package com.example.lensfood1

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapUtils {
    /**
     * Converts a byte array to a Bitmap object.
     *
     * @param byteArray The byte array representing the Bitmap image.
     * @return The converted Bitmap object, or null if conversion fails.
     */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
