package com.example.baseclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.example.DemoProjectApplication
import com.example.customviews.MyProgressDialog
import com.example.data.network.NetworkConnectionInterceptor
import com.example.data.network.repository.ApiRepository
import com.example.db.ProductDatabase
import com.example.ktor.data.network.repository.ApiRepositoryKtor
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel?> : AppCompatActivity() {

    abstract val viewModel: VM
    private var myProgressDialog: MyProgressDialog? = null

    @Inject
    lateinit var localDatabaseInstance: ProductDatabase

    @Inject
    lateinit var apiRepository: ApiRepository


    @Inject
    lateinit var apiRepositoryKtor: ApiRepositoryKtor


    @Inject
    lateinit var networkInterceptor: NetworkConnectionInterceptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (viewModel as BaseViewModel?)?.errorMessage?.observe(this) {
            dismissProgressDialog()
            it.getContentIfNotHandled()?.let { error ->
                toast(error)
            }
        }
        (viewModel as BaseViewModel?)?.unauthorizedUserHandler?.observe(this) {
            dismissProgressDialog()
            it.getContentIfNotHandled()?.let { error ->
                toast(error)
            }
        }

        fun getRepository() = (application as DemoProjectApplication).getRepository()

    }

    fun <T : ViewDataBinding> setDataBindingView(layoutId: Int): T {
        return DataBindingUtil.setContentView(this@BaseActivity, layoutId)
    }

    fun showProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog?.show()
        } else {
            myProgressDialog = MyProgressDialog.show(this, "")
        }
    }

    fun dismissProgressDialog() {
        myProgressDialog?.dismiss()
    }

    abstract fun getLayoutId(): Int
}