package com.ltthuc.feature.presentation.adapter

import com.ltthuc.feature.R
import com.ltthuc.feature.databinding.ItemNotificationBinding
import com.ltthuc.feature.domain.entity.NotificationModel
import com.ltthuc.ui.adapter.BaseViewHolder
import com.ltthuc.ui.adapter.SingleLayoutAdapter

class NotificationsAdapter(onViewClicked: ((Int, (NotificationModel)) -> Unit)): SingleLayoutAdapter<NotificationModel, ItemNotificationBinding>(
    R.layout.item_notification, onItemClicked = onViewClicked) {

    override fun onBindViewHolder(
        holder: BaseViewHolder<NotificationModel, ItemNotificationBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.binding.name.text = item.name
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun selectRow(position: Int) {
//        getItem(position).isSelect= !getItem(position).isSelect
//        if (position ==0 && getItem(position).isSelect) {
//            for (i in 1..<itemCount-1) {
//                getItem(i).isSelect = false
//            }
//            notifyDataSetChanged()
//        } else if (position != 0 && getItem(0).isSelect) {
//            getItem(0).isSelect = false
//            notifyItemChanged(0)
//            notifyItemChanged(position)
//        } else {
//            notifyItemChanged(position)
//        }
//
//    }


}