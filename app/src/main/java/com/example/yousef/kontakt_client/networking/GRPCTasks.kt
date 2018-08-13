package com.example.yousef.kontakt_client.networking

import android.util.Log
import com.example.yousef.kontakt_client.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.TimeUnit

/**
 * Created by Yousef on 8/12/2018.
 */
class GRPCTasks{

    companion object {

        var channel:ManagedChannel? = null
        var stub: ContactsGrpc.ContactsBlockingStub? = null

        fun openConnection(hostIP: String = "192.168.184.130", port: Int = 8080): Boolean {
            try {
                channel = ManagedChannelBuilder.forAddress(hostIP, port).usePlaintext().build()
                stub = ContactsGrpc.newBlockingStub(channel)
                return true
            } catch (e: Exception) {
                return false
            }
        }

        fun addContact(contact: Contact): Boolean {
            var res: ProccessResult? = null

            try {
                runBlocking {
                    res = stub!!.addContact(contact)
                }
            } catch (e: Exception) {
                return false
            }

            return res!!.status == ProccessStatus.OK
        }

        fun deleteContact(id: Int): Boolean {
            try {
                return stub!!.deleteContact(ContactID.newBuilder().setValue(id).build()).status == ProccessStatus.OK
            } catch (e: Exception) {
                return false
            }
        }

        fun editContact(contact: Contact): Boolean {
            try {
                val newContact = stub!!.editContact(contact)
                return newContact.id == contact.id && newContact.name == contact.name && newContact.phoneNumber.number == contact.phoneNumber.number
            } catch (e: Exception) {
                Log.d("debug", e.message)
                return false
            }
        }

        fun getAllContacts(): List<Contact>? {
            var contacts: List<Contact>? = null
            val request = Void.newBuilder().build()
            runBlocking {
                val reply = stub?.getAllContacts(request)
                contacts = reply?.contactsList
            }
            return contacts
        }

        fun closeConnection() {
            channel?.shutdown()?.awaitTermination(1, TimeUnit.SECONDS)
        }
    }
}