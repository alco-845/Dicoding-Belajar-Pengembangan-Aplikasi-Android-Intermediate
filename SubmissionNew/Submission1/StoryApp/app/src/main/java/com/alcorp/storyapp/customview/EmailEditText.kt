package com.alcorp.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
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
        addTextChangedListener(onTextChanged = { s, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(s!!).matches()) {
                this@EmailEditText.error = this@EmailEditText.context.getString(R.string.email_validate)
            } else if (s.isEmpty()) {
                this@EmailEditText.error = this@EmailEditText.context.getString(R.string.email_empty)
            }
        })
    }
}