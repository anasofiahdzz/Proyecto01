package com.sodiri.tamarweather.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.sodiri.tamarweather.R

class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val buscarTicket: Button = findViewById(R.id.buscaTicket)

        buscarTicket.setOnClickListener{
            Toast.makeText(this, "Buscando...", Toast.LENGTH_LONG).show()
        }

        val pantallaInicial : Button = findViewById(R.id.regreso)
        pantallaInicial.setOnClickListener {
            val regreso: Intent = Intent(this, MainActivity::class.java)
            startActivity(regreso)
        }
    }
}