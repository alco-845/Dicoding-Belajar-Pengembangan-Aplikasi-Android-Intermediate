package com.alcorp.storyapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.alcorp.storyapp.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hint = context.getString(R.string.email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(doAfterTextChanged { editable ->
            if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
                this@EmailEditText.error = this@EmailEditText.context.getString(R.string.email_validate)
            } else if (editable!!.isEmpty()) {
                this@EmailEditText.error = this@EmailEditText.context.getString(R.string.email_empty)
            }
        })
    }
}