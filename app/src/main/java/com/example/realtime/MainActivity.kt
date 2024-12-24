package com.example.realtime

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.realtime.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private var ringtone: Ringtone? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("message")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                binding.info.text = value.toString()
                if (value.toString() == "1") {
                    playRingtone()
                } else if (value.toString() == "0") {
                    stopRingtone()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Xatolik", Toast.LENGTH_SHORT).show()
            }
        })
        binding.edtInfo.addTextChangedListener {
            reference.setValue(it.toString())
        }
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
        val stopRingtoneDelay = 10000L
        ringtone?.let {
            window.decorView.postDelayed({
                stopRingtone()
            }, stopRingtoneDelay)
        }
    }
    private fun playRingtone() {
        ringtone?.play()
    }

    private fun stopRingtone() {
        ringtone?.stop()
    }
}