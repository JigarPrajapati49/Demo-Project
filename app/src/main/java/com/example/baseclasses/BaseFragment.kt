package com.example.baseclasses

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.customviews.MyProgressDialog
import com.example.data.network.repository.ApiRepository
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, VM : ViewModel?> : Fragment(), BaseInterface {

    /*protected*/ lateinit var binding: T
    abstract val viewModel: VM
    private var myProgressDialog: MyProgressDialog? = null

    @Inject
    lateinit var mActivity: Activity

    @Inject
    lateinit var apiRepository: ApiRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, getFragmentId(), container, false)

        }
        return binding.root
    }


    abstract fun getFragmentId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
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

    override fun onBackClick() {
        findNavController().popBackStack()
    }

    override fun onCancelClick() {
        findNavController().popBackStack()
    }

}