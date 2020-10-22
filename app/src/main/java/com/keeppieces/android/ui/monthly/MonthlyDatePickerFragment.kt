package com.keeppieces.android.ui.monthly

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.keeppieces.android.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MonthlyDatePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonthlyDatePickerFragment : Fragment() {
    private val snackbar: Snackbar = TODO()

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getClearedUtc() : Calendar {
        val utc: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.clear()
        return utc
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initSettings() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar: Calendar = getClearedUtc()
        calendar.timeInMillis = today
        calendar.roll(Calendar.MONTH, 1)
        val nextMonth = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        val januaryOfThisYear = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        val decemberOfThisYear = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar.roll(Calendar.YEAR, 1)
        val oneYearForward = calendar.timeInMillis

        val todayPair = Pair(today, today)
        val nextMonthPair = Pair(nextMonth, nextMonth)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_monthly_date_picker, container, false)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MonthlyDatePickerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MonthlyDatePickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}