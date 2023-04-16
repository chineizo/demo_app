package com.test.demoapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpannableToView(view)
        view.findViewById<AppCompatButton>(R.id.cancelLocationUpdateButton).setOnClickListener {
            stopBootCompleteService(requireContext())
        }
    }


    private fun stopBootCompleteService(context: Context) {
        Intent(context, BootCompleteService::class.java).apply {
            action = BootCompleteService.ACTION_STOP
            context.startService(this)
        }
    }

    private fun setSpannableToView(view: View) {
        val textToAttachListener = getString(R.string.span_this_string)

        val spannableString = SpannableString(getString(R.string.hello_world)).apply {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(context, getString(R.string.hello_world), Toast.LENGTH_LONG)
                        .show()
                }
            }
            setSpan(
                clickableSpan,
                0,
                textToAttachListener.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        view.findViewById<TextView>(R.id.id_text_view).apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }
}