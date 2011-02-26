/**
 * Author : James Elsey
 * Date : 26/Feb/2011
 * Title : TextToSpeechDemo
 * URL : Http://www.JamesElsey.co.uk
 */
package com.jameselsey.demo.texttospeechdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;

/**
 * This class demonstrates checking for a TTS engine, and if one is
 * available it will spit out some speak based on what is in the
 * text field.
 */
public class Main extends Activity implements TextToSpeech.OnInitListener
{
    private TextToSpeech mTts;
    // This code can be any value you want, its just a checksum.
    private static final int MY_DATA_CHECK_CODE = 1234;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Fire off an intent to check if a TTS engine is installed
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

    }

    /**
     * This method is bound to the speak button so is invoked anytime that is clicked.
     * @param v View default view.
     */
    public void speakClicked(View v)
    {
        // grab the contents of the text box.
        EditText editText = (EditText) findViewById(R.id.inputText);

        mTts.speak(editText.getText().toString(),
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
    }
    /**
     * Executed when a new TTS is instantiated. We don't do anything here since
     * our speech is now determine by the button click
     * @param i
     */
    public void onInit(int i)
    {

    }


    /**
     * This is the callback from the TTS engine check, if a TTS is installed we
     * create a new TTS instance (which in turn calls onInit), if not then we will
     * create an intent to go off and install a TTS engine
     * @param requestCode int Request code returned from the check for TTS engine.
     * @param resultCode int Result code returned from the check for TTS engine.
     * @param data Intent Intent returned from the TTS check.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    /**
     * Be kind, once you've finished with the TTS engine, shut it down so other
     * applications can use it without us interfering with it :)
     */
    @Override
    public void onDestroy()
    {
        // Don't forget to shutdown!
        if (mTts != null)
        {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }
}
