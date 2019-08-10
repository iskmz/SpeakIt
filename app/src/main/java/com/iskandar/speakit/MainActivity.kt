package com.iskandar.speakit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

data class CustomVoice(val name:String, val pitch:Float, val speed:Float)
val voices = listOf(
    CustomVoice("Female (default)",1f,1f),
    CustomVoice("Robot",0.25f,0.8f),
    CustomVoice("Male",0.5f,1f),
    CustomVoice("Crying Fly",3f,1f)
)


class MainActivity : AppCompatActivity() {

    lateinit var selectedVoice : CustomVoice
    lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedVoice = voices[0] // default voice (female)
        init_tts()
        setListeners()
    }

    private fun init_tts() {
        tts = TextToSpeech(this@MainActivity){
            if(it!=TextToSpeech.ERROR)
            {
                tts.language = Locale.ENGLISH
            }
        }
    }

    private fun setListeners() {

        btnClearTxt.setOnClickListener { txtInput.setText("") }
        btnInfo.setOnClickListener {  showInfoDialog() }
        btnExit.setOnClickListener { finish() }

        btnVoices.setOnClickListener { showVoicesDialog() }
        btnSpeak.setOnClickListener { speak() }
    }

    private fun speak() {
        if(txtInput.text.toString().isNotBlank()) {
            tts.speak(txtInput.text.toString(),TextToSpeech.QUEUE_FLUSH,null,"sayingSomething")
        } else {
            tts.speak("No Text Was Entered!",TextToSpeech.QUEUE_FLUSH,null,"sayingNothing")
        }
    }

    private fun showVoicesDialog() {
        AlertDialog.Builder(this@MainActivity)
            .setIcon(R.drawable.ic_voice)
            .setItems(voices.map { it.name }.toTypedArray()){
                dialog, which -> changeVoice(voices[which]); dialog.dismiss()
                Toast.makeText(this@MainActivity,"Voice changed to ${selectedVoice.name}",Toast.LENGTH_LONG).show()
            }
            .setTitle("Choose a Different Voice")
            .show()
    }

    private fun changeVoice(selected: CustomVoice) {
        selectedVoice = selected
        tts.setPitch(selectedVoice.pitch)
        tts.setSpeechRate(selectedVoice.speed)
    }

    private fun showInfoDialog() {
        val info = AlertDialog.Builder(this@MainActivity)
            .setTitle("Speak It !")
            .setMessage("by Iskandar Mazzawi \u00a9")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()  }
            .setIcon(R.drawable.ic_info_green)
            .create()
        info.setCanceledOnTouchOutside(false)
        info.show()

        tts.speak("Speak It !! \n by Iskandar Mazzawi",TextToSpeech.QUEUE_FLUSH,null,"sayingInfo")
    }

    override fun onPause() {
        tts.stop()
        super.onPause()
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}