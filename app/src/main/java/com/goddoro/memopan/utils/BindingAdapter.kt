package com.goddoro.memopan.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


/**
 * created By DORO 5/20/21
 */

@BindingAdapter("android:visibility")
fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:swiperefreshlayout_refreshing")
fun SwipeRefreshLayout.setRefreshingBinding(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}