package com.jintoga.currencyconverter.ui.converter.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jintoga.currencyconverter.R
import com.jintoga.currencyconverter.entity.currencyrates.Rate
import com.jintoga.currencyconverter.inflate
import kotlinx.android.synthetic.main.item_currency_converter.view.*
import java.util.*
import java.util.regex.Pattern


class CurrencyConverterAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder>() {

    interface ActionListener {
        fun onRateFocusChanged(rate: Rate)
        fun onRateValueChanged(rateValue: Double)
    }

    private val rates = ArrayList<Rate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_currency_converter)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rates[position]
        holder.bindData(item)
    }

    fun updateRates(rates: List<Rate>) {
        val diffResult = DiffUtil.calculateDiff(CurrencyRatesDiffCallback(this.rates, rates))
        diffResult.dispatchUpdatesTo(this)
        this.rates.clear()
        this.rates.addAll(rates)
    }

    fun moveItemToFirstPosition(rate: Rate) {
        val selectedPosition = rates.indexOf(rate)
        if (rates.remove(rate)) {
            rates.add(0, rate)
            notifyItemMoved(selectedPosition, 0)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun setActionListener(rate: Rate) = with(itemView) {
            itemView.setOnClickListener {
                //This will trigger OnFocusChanged event of EditText
                currencyValue.requestFocus()
            }
            currencyValue.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    actionListener.onRateFocusChanged(rate)
                    currencyValue.selectAll()
                    showSoftKeyboard()
                } else {
                    hideSoftKeyboard()
                }
            }
            currencyValue.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    val rateValueString = currencyValue.text?.toString()
                    if (rateValueString != null
                            && Pattern.matches("\\d+\\.?\\d*", rateValueString)) {
                        val rateValue = rateValueString.toDouble()
                        actionListener.onRateValueChanged(rateValue)
                    } else if (rateValueString != null
                            && rateValueString.isNotEmpty()) {
                        currencyValue.error = context.getString(R.string.invalid_format)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }

        fun bindData(rate: Rate) = with(itemView) {
            currencyCode.text = rate.code
            currencyValue.keyListener = DigitsKeyListener.getInstance("0123456789.")
            currencyValue.setText(String.format("%.2f", rate.value))
            setActionListener(rate)
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