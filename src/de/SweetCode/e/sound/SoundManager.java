package de.SweetCode.e.sound;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {

    private final int bufferSize;

    public SoundManager(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void test() throws IOException {

        // open the sound file as a Java input stream
        String gongFile = "C:\\Users\\Yonas\\Music\\Kollegah\\Zuhältertape Vol. 4\\05-John Gotti.wav";
        InputStream in = new FileInputStream(gongFile);

        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(in);

        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);

    }

}
