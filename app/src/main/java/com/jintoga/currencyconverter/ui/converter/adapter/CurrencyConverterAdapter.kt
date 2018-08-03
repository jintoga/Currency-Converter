package com.jintoga.currencyconverter.ui.converter.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jintoga.currencyconverter.R
import com.jintoga.currencyconverter.entity.currencyrates.Rate
import com.jintoga.currencyconverter.inflate
import com.jintoga.currencyconverter.ui.converter.CurrencyConverterPresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_currency_converter.view.*
import java.util.*
import java.util.regex.Pattern


class CurrencyConverterAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder>() {

    interface ActionListener {
        fun onBaseCurrencyChanged(rate: Rate)
    }

    companion object {
        private const val INIT_AMOUNT = 100.0
    }

    private val currentRates = ArrayList<Rate>()
    private val amountEditTexts = HashMap<String, EditText>()
    private var baseAmount = INIT_AMOUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_currency_converter)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = currentRates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rate = currentRates[position]
        holder.bindData(rate)
        holder.setClickListener()
        holder.setFocusChangedListener(rate)
        amountEditTexts[rate.code] = holder.itemView.amountEditText
    }

    private fun ViewHolder.setFocusChangedListener(rate: Rate) {
        val amountEditText = itemView.amountEditText
        amountEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val selectedPosition = currentRates.indexOf(rate)
                if (currentRates.remove(rate)) {
                    currentRates.add(0, rate)
                    notifyItemMoved(selectedPosition, 0)
                }
                actionListener.onBaseCurrencyChanged(rate)
                amountEditText.selectAll()
                val disposable = getAmountEditTextSubscription(rate, amountEditText)
                setAmountEditTextDisposable(disposable)
                showSoftKeyboard()
            } else {
                hideSoftKeyboard()
                disposeAmountEditText()
            }
        }
    }

    fun initRates(rates: List<Rate>) {
        currentRates.addAll(rates)
        notifyDataSetChanged()
    }

    fun updateRates(newRates: List<Rate>) {
        currentRates.forEach loop@{ currentRate ->
            newRates.forEach { newRate ->
                if (currentRate.code == newRate.code) {
                    currentRate.value = newRate.value
                    return@loop
                }
            }
        }
        refreshEditTexts()
    }

    private fun refreshEditTexts() {
        for (code in amountEditTexts.keys) {
            val value = getCurrencyValue(code)
            if (value != null) {
                updateCurrencyValue(code, value)
            }
        }
    }

    private fun getCurrencyValue(code: String): Double? =
            currentRates.firstOrNull { it.code == code }?.value

    private fun updateCurrencyValue(code: String, value: Double) {
        val amountEditText = amountEditTexts[code]
        var isFirstEditText = false
        if (currentRates.isNotEmpty()) {
            val firstEditText = currentRates[0]
            if (firstEditText.code == code) {
                isFirstEditText = true
            }
        }
        if (!isFirstEditText
                && amountEditText != null
                && value != CurrencyConverterPresenter.BASE_VALUE) {
            amountEditText.bindValue(value)
        }
    }

    private fun EditText.bindValue(value: Double) {
        val calculatedAmount = value * baseAmount
        //Locale.ROOT for using '.' as decimal separator
        setText(String.format(Locale.ROOT, "%.2f", calculatedAmount))
    }

    private fun getAmountEditTextSubscription(rate: Rate, editText: EditText): Disposable =
            RxTextView.textChanges(editText)
                    .map {
                        return@map parseAmountText(it.toString(), rate)
                    }
                    .doOnNext({
                        baseAmount = it
                        refreshEditTexts()
                    })
                    .subscribe()

    private fun parseAmountText(text: String, rate: Rate): Double {
        var parsedValue = 0.0
        if (isParsableText(text)) {
            val currentAmount = text.toDouble()
            //Base Currency changed so BaseAmount changed
            if (currentAmount == baseAmount) {
                parsedValue = baseAmount
            } else {
                val value = rate.value
                if (value > 0.0) {
                    parsedValue = currentAmount / value
                }
            }
        }
        return parsedValue
    }

    /**
     * check if text is parsable to Double with '.' being decimal separator symbol
     */
    private fun isParsableText(text: String) =
            Pattern.matches("\\d+\\.?\\d*", text)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var amountEditTextDisposable: Disposable? = null

        fun bindData(rate: Rate) = with(itemView) {
            currencyCode.text = rate.code
            amountEditText.bindValue(rate.value)
        }

        fun setClickListener() = with(itemView) {
            itemView.setOnClickListener {
                //Change BaseAmount to amount of clicked ItemView's EditText
                val currentAmountString = amountEditText.text.toString()
                baseAmount = if (isParsableText(currentAmountString)) {
                    currentAmountString.toDouble()
                } else {
                    0.0
                }
                //This will trigger OnFocusChanged event of EditText
                amountEditText.requestFocus()
            }
        }

        fun disposeAmountEditText() {
            amountEditTextDisposable?.dispose()
        }

        fun setAmountEditTextDisposable(disposable: Disposable) {
            this@ViewHolder.amountEditTextDisposable = disposable
        }

        fun showSoftKeyboard() = with(itemView) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(amountEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        fun hideSoftKeyboard() = with(itemView) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(amountEditText.windowToken, 0)
        }

    }
}