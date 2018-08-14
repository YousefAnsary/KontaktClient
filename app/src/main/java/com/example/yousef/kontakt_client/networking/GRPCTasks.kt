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

        private var channel:ManagedChannel? = null
        private var stub: ContactsGrpc.ContactsBlockingStub? = null

        suspend fun openConnection(hostIP: String = "192.168.184.130", port: Int = 8080): Boolean {
            try {
                channel = ManagedChannelBuilder.forAddress(hostIP, port).usePlaintext().build()
                stub = ContactsGrpc.newBlockingStub(channel)
            } catch (e: Exception) {
                Log.e("debug", e.message)
                return false
            }
            return true
        }

        suspend fun addContact(contact: Contact): Boolean {
            var res: ProccessResult? = null

            try {
                res = stub!!.addContact(contact)
            } catch (e: Exception) {
                Log.e("debug", e.message)
                return false
            }

            return res!!.status == ProccessStatus.OK
        }

        suspend fun deleteContact(id: Int): Boolean {
            var isDeleted = false
            try {
                isDeleted = stub!!.deleteContact(ContactID.newBuilder().setValue(id).build()).status == ProccessStatus.OK
            } catch (e: Exception) {
                Log.e("debug", e.message)
                return false
            }
            return isDeleted
        }

        suspend fun editContact(contact: Contact): Boolean {
            var isEdited = false
            try {
                val newContact = stub!!.editContact(contact)
                isEdited = newContact.id == contact.id && newContact.name == contact.name && newContact.phoneNumber.number == contact.phoneNumber.number
            } catch (e: Exception) {
                Log.e("debug", e.message)
                return false
            }
            return isEdited
        }

        suspend fun getAllContacts(): List<Contact>? {
            var contacts: List<Contact>? = null
            val request = Void.newBuilder().build()
            try {
                val reply = stub!!.getAllContacts(request)
                contacts = reply?.contactsList
            } catch (e: Exception) {
                Log.e("debug", e.message)
                return null
            }

            return contacts
        }

        suspend fun closeConnection(): Boolean {
            try {
                channel!!.shutdown()!!.awaitTermination(1, TimeUnit.SECONDS)
            } catch (e: Exception) {
                //Log.e("debug", e.message)
                return false
            }
            return true
        }
    }
}