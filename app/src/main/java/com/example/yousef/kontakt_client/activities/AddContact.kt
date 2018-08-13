package com.example.yousef.kontakt_client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.yousef.kontakt_client.Contact
import com.example.yousef.kontakt_client.NumberType
import com.example.yousef.kontakt_client.PhoneNumber
import com.example.yousef.kontakt_client.R
import com.example.yousef.kontakt_client.networking.GRPCTasks
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast

class AddContact : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        add_contact_btn.setOnClickListener { addContact() }
    }

    private fun addContact() {
        val phoneNumber = PhoneNumber.newBuilder().setNumber(phone_edt.text.toString()).setType(NumberType.MOBILE).build()
        val newContact = Contact.newBuilder().setName(name_edt.text.toString()).setPhoneNumber(phoneNumber).build()
        launch {
            if (GRPCTasks.addContact(newContact)) {
                launch(UI) {
                    toast("inserted")
                    onBackPressed()
                }
            } else {
                launch(UI) {
                    toast("failed")
                }
            }
        }

    }
}
