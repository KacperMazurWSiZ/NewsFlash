package com.example.newsflash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.newsflash.R

class Contact : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var selectedImageNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val buttonSend = view.findViewById<View>(R.id.buttonSend)
        val buttonUpload = view.findViewById<View>(R.id.buttonUpload)
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextMessage = view.findViewById<EditText>(R.id.editTextMessage)
        selectedImageNameTextView = view.findViewById(R.id.selectedImageNameTextView)

        buttonSend.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val message = editTextMessage.text.toString()

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                // Przynajmniej jedno z pól jest puste, wyświetl komunikat błędu
                Toast.makeText(requireContext(), "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sprawdź poprawność adresu e-mail
            if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "Nieprawidłowy adres e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Twórz intencję wysyłki e-maila i tak dalej...
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:generator.frajdy1@o2.pl")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Form Submission")
            intent.putExtra(Intent.EXTRA_TEXT, "Name: $name\nEmail: $email\nMessage: $message")

            // Attach the image if available
            selectedImageUri?.let { imageUri ->
                intent.putExtra(Intent.EXTRA_STREAM, imageUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(intent)
        }

        buttonUpload.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }

        return view
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val imageFileName = getFileName(selectedImageUri!!)
            selectedImageNameTextView.text = imageFileName
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                return displayName
            }
        }
        return ""
    }
}