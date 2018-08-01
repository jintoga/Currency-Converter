package com.jintoga.currencyconverter.ui.converter.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jintoga.currencyconverter.R
import com.jintoga.currencyconverter.entity.Rate
import com.jintoga.currencyconverter.inflate
import kotlinx.android.synthetic.main.item_currency_converter.view.*

class CurrencyConverterAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder>() {

    interface ActionListener {
        fun onRateFocusChange(rate: Rate)
    }

    private val rates = ArrayList<Rate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_currency_converter)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rates[position]
        holder.bindData(item, actionListener)
    }

    fun bindData(rates: List<Rate>) {
        this.rates.clear()
        this.rates.addAll(rates)
        notifyDataSetChanged()
    }

    fun moveItemToFirstPosition(rate: Rate) {
        val selectedPosition = rates.indexOf(rate)
        if (rates.remove(rate)) {
            rates.add(0, rate)
            notifyItemMoved(selectedPosition, 0)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(rate: Rate, actionListener: ActionListener) = with(itemView) {
            currencyCode.text = rate.code
            currencyValue.setText(rate.value.toString())
            itemView.setOnClickListener {
                //This will trigger OnFocusChanged event of EditText
                currencyValue.requestFocus()
            }
            currencyValue.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    actionListener.onRateFocusChange(rate)
                    currencyValue.selectAll()
                    showSoftKeyboard()
                } else {
                    hideSoftKeyboard()
                }
            }
        }

        private fun showSoftKeyboard() = with(itemView) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(currencyValue, InputMethodManager.SHOW_IMPLICIT)
        }

        private fun hideSoftKeyboard() = with(itemView) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currencyValue.windowToken, 0)
        }

    }

}