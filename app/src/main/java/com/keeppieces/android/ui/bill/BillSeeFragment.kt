package com.keeppieces.android.ui.bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import kotlinx.android.synthetic.main.fragment_bill_see.*

class BillSeeFragment(val bill: Bill) : Fragment() {
    private lateinit var viewModel : BillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        arguments?.let {
//            bill = Bill(it.getString("billTime").toString(),
//                it.getDouble("billAmount"),
//                it.getString("billAccount").toString(),
//                it.getString("billMember").toString(),
//                it.getString("billPrimary").toString(),
//                it.getString("billSecondary").toString(),
//                it.getString("billType").toString())
//        }
        return inflater.inflate(R.layout.fragment_bill_see, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val billTime: TextView = view.findViewById(R.id.billTime)
        val billAmount: TextView = view.findViewById(R.id.billAmount)
        val billAccount: TextView = view.findViewById(R.id.billAccount)
        val billMember: TextView = view.findViewById(R.id.billMember)
        val billPrimaryCategory: TextView = view.findViewById(R.id.billPrimary)
        val billSecondaryCategory: TextView = view.findViewById(R.id.billSecondary)
        val billType: TextView = view.findViewById(R.id.billType)

        billTime.text = bill.date
        billAmount.text = bill.amount.toString()
        billAccount.text = bill.account
        billMember.text = bill.member
        billPrimaryCategory.text = bill.secondaryCategory
        billSecondaryCategory.text = bill.primaryCategory
        billType.text = bill.type
    }

//    private fun billAddFragmentUpdate() {
//        Log.d("BillActivity","billAddUpdate")
//        val fragment = BillAddFragment(bill)
//        val transaction = activity?.supportFragmentManager?.beginTransaction()
//        transaction?.apply {
//            replace(R.id.bill_main, fragment)
//            commit()
//        }
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        bill_edit.setOnClickListener {
//            if (activity != null) {
//                val activity = activity as BillActivity
//                activity.billAddFragmentUpdate(bill)
//            } else {
//                Log.d("BillActivity", "no activity")
//            }
            billAddUpdateFragment()
        }

        bill_back.setOnClickListener {
            activity?.finish()
        }

        billDeleteBtn.setOnClickListener {
            viewModel.deleteBill(bill)
            activity?.finish()
        }
    }

    private fun billAddUpdateFragment() {
        context?.let {
            BillActivity.start(it, bill,2)
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(inBill: Bill) =
//            BillSeeFragment().apply {
//                arguments = Bundle().apply {
//                    putString("billTime", inBill.date)
//                    putDouble("billAmount", inBill.amount)
//                    putString("billAccount", inBill.account)
//                    putString("billMember", inBill.member)
//                    putString("billPrimary", inBill.primaryCategory)
//                    putString("billSecondary", inBill.secondaryCategory)
//                    putString("billType", inBill.type)
//                }
//            }
//    }
}