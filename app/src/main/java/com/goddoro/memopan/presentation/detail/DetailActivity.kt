package com.goddoro.memopan.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.goddoro.memopan.R
import com.goddoro.memopan.databinding.ActivityDetailBinding
import com.goddoro.memopan.utils.observeOnce
import com.goddoro.memopan.utils.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityDetailBinding

    private val mViewModel : DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDetailBinding.inflate(LayoutInflater.from(this))

        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = this

        setContentView(mBinding.root)

        observeViewModel()
        checkBody()
    }

    private fun observeViewModel(){

        mViewModel.apply {

            onSaveComplete.observeOnce(this@DetailActivity){
                Toast.makeText(this@DetailActivity,"메모 저장을 완료했습니다",Toast.LENGTH_SHORT).show()
                finish()
            }

            clickFinish.observeOnce(this@DetailActivity){
                onBackPressed()
            }
            errorInvoked.observeOnce(this@DetailActivity){
                Toast.makeText(this@DetailActivity,it.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkBody() {

        val body = intent?.getStringExtra(ARG_BODY)
        if ( body != null) {
            mViewModel.body.value = body
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()


        this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)

    }
    companion object {

        const val ARG_BODY = "ARG_BODY"
    }

}