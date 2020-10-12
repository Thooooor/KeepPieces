package com.keeppieces.android.logic

import com.keeppieces.android.R.color.*

object Repository {
    fun getColorInt(color: String, index: Int) = when (color) {
        "green" -> when (index % 10) {
            0 -> green_900
            1 -> green_800
            2 -> green_700
            3 -> green_600
            4 -> green_500
            5 -> green_400
            6 -> green_300
            7 -> green_200
            8 -> green_100
            9 -> green_50
            else -> dark_green
        }
        "purple" -> when (index % 10) {
            0 -> purple_900
            1 -> purple_800
            2 -> purple_700
            3 -> purple_600
            4 -> purple_500
            5 -> purple_400
            6 -> purple_300
            7 -> purple_200
            8 -> purple_100
            9 -> purple_50
            else -> purple
        }
        "blue" -> when (index % 10) {
            0 -> blue_900
            1 -> blue_800
            2 -> blue_700
            3 -> blue_600
            4 -> blue_500
            5 -> blue_400
            6 -> blue_300
            7 -> blue_200
            8 -> blue_100
            9 -> blue_50
            else -> blue
        }
        "orange" -> when (index % 10) {
            0 -> orange_900
            1 -> orange_800
            2 -> orange_700
            3 -> orange_600
            4 -> orange_500
            5 -> orange_400
            6 -> orange_300
            7 -> orange_200
            8 -> orange_100
            9 -> orange_50
            else -> orange
        }
        "yellow" -> when (index % 10) {
            0 -> yellow_900
            1 -> yellow_800
            2 -> yellow_700
            3 -> yellow_600
            4 -> yellow_500
            5 -> yellow_400
            6 -> yellow_300
            7 -> yellow_200
            8 -> yellow_100
            9 -> yellow_50
            else -> yellow
        }
        else -> when (index % 10) {
            0 -> green_900
            1 -> green_800
            2 -> green_700
            3 -> green_600
            4 -> green_500
            5 -> green_400
            6 -> green_300
            7 -> green_200
            8 -> green_100
            9 -> green_50
            else -> dark_green
        }
    }
}
