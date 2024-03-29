package com.example.baseclasses

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.example.customviews.MyProgressDialog
import com.example.data.network.NetworkConnectionInterceptor
import com.example.demoproject.R
import com.example.utils.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

/**
 * Created by Mittal Varsani on 23/11/21.
 * @author Mittal Varsani
 */

abstract class BaseBottomSheetDialogFragment<T : ViewDataBinding,VM :ViewModel?> :
    BottomSheetDialogFragment() {
    /*protected*/ lateinit var binding: T
    private var myProgressDialog: MyProgressDialog? = null
    lateinit var mActivity: Activity
    abstract val viewModel: VM?

//    @Inject
//    lateinit var preferenceManager: PreferenceManager


    @Inject
    lateinit var networkInterceptor: NetworkConnectionInterceptor


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getDialogFragmentId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (viewModel as BaseViewModel?)?.errorMessage?.observe(viewLifecycleOwner) {
            dismissProgressDialog()
            it.getContentIfNotHandled()?.let { error ->
                context?.toast(error)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }



    abstract fun getDialogFragmentId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.Theme_DemoProject_Slide)
        }
    }


    fun showProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog?.show()
        } else {
            myProgressDialog = MyProgressDialog.show(mActivity, "")
        }
    }

    fun dismissProgressDialog() {
        myProgressDialog?.dismiss()
    }


}