package com.example.yousef.kontakt_client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.yousef.kontakt_client.*
import com.example.yousef.kontakt_client.networking.GRPCTasks
import kotlinx.android.synthetic.main.activity_contact_details.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast

class ContactDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        val contactDetails = intent.getStringArrayListExtra("contactDetails")

        val id = contactDetails[0]
        val name = contactDetails[1]
        val number = contactDetails[2]

        name_edit_edt.setText(name)
        phone_edit_edt.setText(number)

        edit_btn.setOnClickListener {
            val phoneNumber = PhoneNumber.newBuilder().setNumber(phone_edit_edt.text.toString()).setType(NumberType.MOBILE).build()
            val newContact = Contact.newBuilder().setId(ContactID.newBuilder().setValue(id.toInt()).build())
                             .setName(name_edit_edt.text.toString()).setPhoneNumber(phoneNumber).build()
            launch {
                var message = "edited"
                if (!GRPCTasks.editContact(newContact)) {
                    message = "Something wrong happened"
                }
                launch(UI) {
                    toast(message)
                    onBackPressed()
                }
            }
        }

        delete_btn.setOnClickListener {
            launch {
                var message = "deleted"
                if (!GRPCTasks.deleteContact(id.toInt())) {
                    message = "Something wrong happened"
                }
                launch(UI) {
                    toast(message)
                    onBackPressed()
                }
            }
        }
    }
}
