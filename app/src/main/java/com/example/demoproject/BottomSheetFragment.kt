package com.example.demoproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.baseclasses.BaseBottomSheetDialogFragment
import com.example.demoproject.databinding.FragmentBottomSheetBinding
import com.example.ui.BottomSheetPresenter
import com.example.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentBottomSheetBinding, BottomSheetViewModel>() ,BottomSheetPresenter{


    override fun getDialogFragmentId(): Int {
        return R.layout.fragment_bottom_sheet
    }

    override val viewModel: BottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.presenter = this

    }

    companion object {
        private const val TAG = "BottomSheetFragment"
        fun show(
            fragmentManager: FragmentManager
        ) {
            val addStaffDialog = BottomSheetFragment()

            addStaffDialog.show(fragmentManager, TAG)
        }
    }

    override fun onCloseClick() {
        dismiss()
    }


}