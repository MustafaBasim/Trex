package com.mustafa.trexdinogame

import android.graphics.Bitmap

class Object (
    val image : Bitmap,
    var x: Int = 0,
    var y: Int = 0,
    columns: Int = 0) {
    var width: Int = 0
    var height: Int = 0
    init {
        width = image.width / columns
        height = image.height
    }
}