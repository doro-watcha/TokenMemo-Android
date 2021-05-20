package com.goddoro.memopan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.goddoro.memopan.databinding.ItemMemoBinding
import com.goddoro.memopan.room.Memo
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * created By DORO 5/18/21
 */



class MemoBindingAdapter : RecyclerView.Adapter<MemoBindingAdapter.MemoViewHolder>(){

    private val onClickMemo: PublishSubject<Memo> = PublishSubject.create()
    val clickMemo: Observable<Memo> = onClickMemo


    private val diff = object : DiffUtil.ItemCallback<Memo>() {
        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diff)

    fun submitItems(items: List<Memo>?) {
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMemoBinding.inflate(inflater, parent, false)
        return MemoViewHolder(binding)

    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    inner class MemoViewHolder ( val binding : ItemMemoBinding ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                onClickMemo.onNext(differ.currentList[layoutPosition])
            }

        }

        fun bind ( item : Memo) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()


        }

    }

}

@BindingAdapter("app:recyclerview_memo_list")
fun RecyclerView.setMemoList(items : List<Memo>?){
    (adapter as? MemoBindingAdapter)?.run {
        this.submitItems(items)
    }
}