package com.goddoro.memopan.presentation.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentManager
import com.goddoro.memopan.R
import com.goddoro.memopan.databinding.DialogBottomClipboardBinding
import com.goddoro.memopan.utils.AppPreference
import com.goddoro.memopan.utils.Broadcast
import com.goddoro.memopan.utils.ScreenUtil
import com.goddoro.memopan.utils.observeOnce
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * created By DORO 5/19/21
 */

class ClipBoardBottomSheetDialog (
    val body : String
): BottomSheetDialogFragment() {


    private lateinit var mBinding : DialogBottomClipboardBinding

    private val mViewModel : ClipBoardDialogViewModel by viewModel()
    private val screenUtil : ScreenUtil by inject()
    private val appPreference : AppPreference by inject()
    override fun getTheme(): Int = R.style.BottomSheetDialog


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)

        bottomSheetDialog.behavior.peekHeight = screenUtil.screenHeight

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogBottomClipboardBinding.inflate(inflater, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel


        initSetting()
        observeViewModel()

    }

    private fun initSetting() {

        mViewModel.body.value = body
    }
    private fun observeViewModel() {

        mViewModel.apply {

            clickSaveButton.observeOnce(this@ClipBoardBottomSheetDialog){
                Broadcast.moveToDetailBroadcast.onNext(mViewModel.body.value ?: "")
                dismiss()
            }

            clickDismiss.observeOnce(this@ClipBoardBottomSheetDialog){
                dismiss()
            }
        }
    }



    companion object {
        fun show(
            fm: FragmentManager,
            body: String
        ) {
            val dialog = ClipBoardBottomSheetDialog(body)
            dialog.show(fm, dialog.tag)
        }
    }


}