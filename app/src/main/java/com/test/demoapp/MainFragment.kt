package com.test.demoapp

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private val locationCacheManager: LocationCacheManager by lazy {
        LocationCacheManager(
            requireContext()
        )
    }

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

        locationCacheManager.getLocation().stateIn(lifecycleScope, SharingStarted.Lazily, initialValue = null)



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