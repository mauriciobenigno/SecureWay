package com.mauriciobenigno.secureway.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {

        fun getFormatedDate(date: Date): String {
           return  SimpleDateFormat("dd/MM/yyyy").format(Date())
        }

    }
}