package com.goddoro.memopan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
            ClipBoardBottomSheetDialog.show(supportFragmentManager, clipBoardUtil.getClipBoard())
            appPreference.needToCheckClipBoard = false
        }

    }
    private fun setupRecyclerView () {

        val touchCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                mViewModel.removeMemo(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(mBinding.recyclerview)

        mBinding.recyclerview.apply {

            adapter = MemoBindingAdapter().apply {

                clickMemo.subscribe{
                    clipBoardUtil.copyToClipboard(it.title ?: "",it.body ?: "")
                    appPreference.curAppClipBoard = it.body ?: ""
                    Log.d(TAG, "WOW")
                    toastUtil.createToast("[${it.body}] 가 클립보드에 복사되었습니다")?.show()
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

            onLoadCompleted.observe(this@MainActivity, Observer {
                if ( it && mBinding.layoutRefresh.isRefreshing){
                    mBinding.layoutRefresh.isRefreshing = false

                }
            })
            onRemoveCompleted.observeOnce(this@MainActivity){
                refresh()
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