package com.example.demoproject

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseclasses.BaseActivity
import com.example.demoproject.databinding.ActivitySavedBinding
import com.example.viewmodels.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedActivity : BaseActivity<SavedViewModel>() {
    override val viewModel: SavedViewModel by viewModels()
    lateinit var binding: ActivitySavedBinding
    var countValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = setDataBindingView(R.layout.activity_saved)


    }

    override fun getLayoutId(): Int = R.layout.activity_saved

//    private fun setupRecyclerView() {
//        binding.rvTitle.layoutManager = LinearLayoutManager(this)
//        binding.rvTitle.adapter = RecyclerAdapter()
//        binding.rvTitle.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//    }
}