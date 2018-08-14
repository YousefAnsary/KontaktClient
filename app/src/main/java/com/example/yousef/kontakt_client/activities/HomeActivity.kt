package com.example.yousef.kontakt_client.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.yousef.kontakt_client.R
import com.example.yousef.kontakt_client.networking.GRPCTasks
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /* When connect button is pressed close current connection if any, then open a new connection
           with the entered IP and port, if left blank connection will open  on port 8080 and my
            linux VM IP then go to contacts activity */
        connect_btn.setOnClickListener {

            val hostIP = host_ip_edt.text.toString()
            val port = port_edt.text.toString()

            launch {
                runBlocking {
                    GRPCTasks.closeConnection()
                    if (hostIP != "" && port != "") GRPCTasks.openConnection(hostIP, port.toInt())
                    else GRPCTasks.openConnection()
                }
                launch(UI) { startActivity(Intent(this@HomeActivity, MainActivity::class.java)) }
            }
        }
    }
}
