package uz.mlsoft.newmypuzzle15.utils

import android.graphics.Paint
import android.widget.TextView

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

