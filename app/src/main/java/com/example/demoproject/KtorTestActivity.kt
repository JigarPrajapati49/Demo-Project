package com.example.demoproject

import android.os.Bundle
import androidx.activity.viewModels
import com.example.baseclasses.BaseActivity
import com.example.demoproject.databinding.ActivityKtorTestBinding
import com.example.ktor.ui.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KtorTestActivity : BaseActivity<TestViewModel>() {
    override val viewModel: TestViewModel by viewModels()
    lateinit var binding: ActivityKtorTestBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setDataBindingView(R.layout.activity_ktor_test)


//        viewModel.getDummyData()


//        viewModel.getData.observe(this) {
//            Log.e("TAG", "onCreate: -------------${it.size}")
//        }
//        viewModel.productData.observe(this) {
//            Log.e("TAG", "onCreate: -------------Product${it.data.productList}")
//        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ktor_test
    }
}