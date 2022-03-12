package com.example.mypocket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mypocket.databinding.ItemExpenseBinding
import com.example.mypocket.model.Expense

class ExpenseListAdapter : ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(DiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder{

        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemExpenseBinding.inflate(inflate, parent, false)

        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int){

        holder.bind(getItem(position))

    }

    class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
        ): RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Expense){

                binding.txtItemDescription.text = item.descrip
                binding.txtItemValue.text       = item.value
                binding.txtItemDate.text        = item.date
                binding.txtItemParcel.text      = item.parcel
            }
    }
}

class DiffCallBack: DiffUtil.ItemCallback<Expense>(){

    override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id


}