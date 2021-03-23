package com.example.speechtotext

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.*
import com.google.protobuf.ByteString
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val speechClient: SpeechClient by lazy {
        context?.resources?.openRawResource(R.raw.credential).use{
            SpeechClient.create(
                    SpeechSettings.newBuilder()
                            .setCredentialsProvider { GoogleCredentials.fromStream(it) }
                            .build())
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val filePath1 = activity!!.getExternalFilesDir("rec")?.toString() + "/my-rec.amr"
        val gcsUri1 = "gs://cloud-samples-data/speech/brooklyn_bridge.raw"
        val audioBytes = ByteString.copyFrom(File(filePath1).readBytes())

        GlobalScope.launch {
            analyze(gcsUri1,audioBytes)
        }
    }

    private fun analyze(gcsUri : String, file : ByteString) {
        val req = RecognizeRequest.newBuilder()
            .setConfig(
                RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.AMR_WB)
                    .setLanguageCode("en-US")
                    .setSampleRateHertz(16000)
                    .build()
            )
            .setAudio(
                RecognitionAudio.newBuilder()
                    .setContent(file)
                    .build()
            ).build()

        val config = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("en-US")
                .build()
        val audio = RecognitionAudio.newBuilder().setUri(gcsUri).build()

        val response = speechClient.recognize(req)
        val response1 = speechClient.recognize(config, audio)

        val result = response.resultsList
        for (i in result){
            val alt : SpeechRecognitionAlternative = i.alternativesList.get(0)
            Log.d("Raw File", alt.transcript)
        }

        val result1 = response1.resultsList
        for (i in result1){
            val alt : SpeechRecognitionAlternative = i.alternativesList.get(0)
            Log.d("Raw File", alt.transcript)
        }

    }
 }



