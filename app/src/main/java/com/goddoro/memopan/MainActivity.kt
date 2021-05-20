package com.goddoro.memopan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goddoro.memopan.databinding.ActivityMainBinding
import com.goddoro.memopan.presentation.detail.DetailActivity
import com.goddoro.memopan.presentation.dialog.ClipBoardBottomSheetDialog
import com.goddoro.memopan.utils.*
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()

    private lateinit var mBinding : ActivityMainBinding

    private val mViewModel : MainViewModel by viewModel()

    private val clipBoardUtil : ClipBoardUtil by inject()

    private val appPreference : AppPreference by inject()

    private val toastUtil : ToastUtil by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.vm =mViewModel
        mBinding.lifecycleOwner = this

        setContentView(mBinding.root)

        setupRecyclerView()
        setupSwipeRefreshLayout()
        observeViewModel()
        setupBroadcast()

        appPreference.needToCheckClipBoard = true

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        Log.d(TAG, clipBoardUtil.isClipboardAvailable().toString())
        Log.d(TAG, "CLIPBOARD = " + clipBoardUtil.getClipBoard())
        Log.d(TAG, "APP CLIPBOARD = " + appPreference.curAppClipBoard)
        Log.d(TAG, "NEED TO CHECK = "  + appPreference.needToCheckClipBoard)

        if (appPreference.needToCheckClipBoard && clipBoardUtil.isClipboardAvailable() && appPreference.curAppClipBoard != clipBoardUtil.getClipBoard()) {
            ClipBoardBottomSheetDialog.show(supportFragmentManager, appPreference.curAppClipBoard)
            appPreference.needToCheckClipBoard = false
        }

    }
    private fun setupRecyclerView () {

        mBinding.recyclerview.apply {

            adapter = MemoBindingAdapter().apply {

                clickMemo.subscribe{
                    clipBoardUtil.copyToClipboard(it.title ?: "",it.body ?: "")
                    appPreference.curAppClipBoard = it.body ?: ""
                    Log.d(TAG, "WOW")
                    toastUtil.createToast("${it.body} 내용을 복사했습니다")?.show()
                }.disposedBy(compositeDisposable)
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        mBinding.layoutRefresh.setOnRefreshListener {
            mViewModel.refresh()
        }

        mBinding.layoutRefresh.post {
            mViewModel.refresh()
        }
    }

    private fun observeViewModel () {

        mViewModel.apply {


            clickAddMemo.observeOnce(this@MainActivity){

                startActivity(DetailActivity::class, R.anim.slide_in_from_right, R.anim.slide_out_to_left)
            }

            onLoadCompleted.observeOnce(this@MainActivity){
                if (mBinding.layoutRefresh.isRefreshing) {
                    mBinding.layoutRefresh.isRefreshing = false
                }
            }

            errorInvoked.observeOnce(this@MainActivity){
                Log.d(TAG, it.message.toString())
            }
        }
    }

    private fun setupBroadcast() {

        Broadcast.apply {

            saveCompleteBroadcast.subscribe{
                mViewModel.refresh()
            }.disposedBy(compositeDisposable)

            moveToDetailBroadcast.subscribe{
                val intent = Intent ( this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ARG_BODY,it)
                startActivity(intent)
            }.disposedBy(compositeDisposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        appPreference.needToCheckClipBoard = true
    }

    companion object {

        const val ARG_BODY = "ARG_BODY"
    }
}