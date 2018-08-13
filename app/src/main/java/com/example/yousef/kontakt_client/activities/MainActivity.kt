package com.example.yousef.kontakt_client.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.yousef.kontakt_client.*
import com.example.yousef.kontakt_client.networking.GRPCTasks
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    var contacts: List<Contact>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add_contact_fab.setOnClickListener { startActivity(Intent(this, AddContact::class.java)) }
        contacts_list.setOnItemClickListener { _, _, position, _->
                                              val intent = Intent(this, ContactDetails::class.java)
                                              val contactDetails = arrayListOf<String>(contacts!![position].id.value.toString(),
                                                                                       contacts!![position].name,
                                                                                       contacts!![position].phoneNumber.number) //parcelable is better
                                              intent.putExtra("contactDetails", contactDetails)
                                              startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        launch {
            contacts = GRPCTasks.getAllContacts()
            var names = mutableListOf<String>()
            contacts?.map { names.add(it.name) }
            launch(UI) { contacts_list.adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1, names) }
        }
    }

}

/*private class GrpcTask() : AsyncTask<Void, Void, List<Contact>>() {

    var newContact: Contact? = null
    var idToDelete: Int? = null
    constructor(newContact: Contact): this() {
        this.newContact = newContact
    }

    constructor(idToDelete: Int): this(){
        this.idToDelete = idToDelete
    }

    //private val activityReference: WeakReference<Activity> = WeakReference(activity)
    private var channel: ManagedChannel? = null
    override fun doInBackground(vararg params: Void?): List<Contact>? {
        return try {
            channel = ManagedChannelBuilder.forAddress("192.168.184.129", 8080).usePlaintext().build()
            val stub = ContactsGrpc.newBlockingStub(channel)
            val request = Void.newBuilder().build()
            val reply = stub.getAllContacts(request)
            val num: String = reply.contactsList[0].phoneNumber.number
            Log.d("GRPC Client", reply.contactsList.toString())
            val phoneNumber = PhoneNumber.newBuilder().setNumber("20123456789").setType(NumberType.MOBILE).build()
            val newContactRequest = Contact.newBuilder().setName("Yousef").setPhoneNumber(phoneNumber).build()
            val reply2 = stub.addContact(newContactRequest)
            Log.d("GRPC Client", reply2.message.text)

            reply.contactsList
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()

            Log.e("CLIENT", "Failed... : %s".format(sw))
            null
        }
    }
    override fun onPostExecute(result: List<Contact>?) {
        try {
            channel?.shutdown()?.awaitTermination(1, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}*/
