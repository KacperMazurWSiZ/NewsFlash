package com.example.newsflash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class Contact : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val buttonSend = view.findViewById<View>(R.id.buttonSend)
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextMessage = view.findViewById<EditText>(R.id.editTextMessage)

        buttonSend.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val message = editTextMessage.text.toString()

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:generator.frajdy1@o2.pl")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Form Submission")
            intent.putExtra(Intent.EXTRA_TEXT, "Name: $name\nEmail: $email\nMessage: $message")

            startActivity(intent)
        }

        return view
    }
}