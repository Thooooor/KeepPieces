package com.keeppieces.android.logic

import com.keeppieces.android.R.color.*

object Repository {
    fun getColorInt(color: String, index: Int) = when (color) {
        "green" -> when (index % 10) {
            0 -> green_900
            1 -> green_600
            2 -> green_300
            3 -> green_50
            4 -> green_800
            5 -> green_500
            6 -> green_200
            7 -> green_700
            8 -> green_400
            9 -> green_100
            else -> dark_green
        }
        "purple" -> when (index % 10) {
            0 -> purple_900
            1 -> purple_600
            2 -> purple_300
            3 -> purple_50
            4 -> purple_800
            5 -> purple_500
            6 -> purple_200
            7 -> purple_700
            8 -> purple_400
            9 -> purple_100
            else -> purple
        }
        "blue" -> when (index % 10) {
            0 -> blue_900
            1 -> blue_600
            2 -> blue_300
            3 -> blue_50
            4 -> blue_800
            5 -> blue_500
            6 -> blue_200
            7 -> blue_700
            8 -> blue_400
            9 -> blue_100
            else -> blue
        }
        "orange" -> when (index % 10) {
            0 -> orange_900
            1 -> orange_600
            2 -> orange_300
            3 -> orange_50
            4 -> orange_800
            5 -> orange_500
            6 -> orange_200
            7 -> orange_700
            8 -> orange_400
            9 -> orange_100
            else -> orange
        }
        "yellow" -> when (index % 10) {
            0 -> yellow_900
            1 -> yellow_600
            2 -> yellow_300
            3 -> yellow_50
            4 -> yellow_800
            5 -> yellow_500
            6 -> yellow_200
            7 -> yellow_700
            8 -> yellow_400
            9 -> yellow_100
            else -> yellow
        }
        else -> when (index % 10) {
            0 -> green_900
            1 -> green_600
            2 -> green_300
            3 -> green_50
            4 -> green_800
            5 -> green_500
            6 -> green_200
            7 -> green_700
            8 -> green_400
            9 -> green_100
            else -> dark_green
        }
    }
}
