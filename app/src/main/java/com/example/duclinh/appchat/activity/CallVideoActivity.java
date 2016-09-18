package com.example.duclinh.appchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.duclinh.appchat.R;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class CallVideoActivity extends AppCompatActivity {
    public static final String VIDEO_TRACK_ID = "videoPN";
    public static final String AUDIO_TRACK_ID = "audioPN";
    public static final String LOCAL_MEDIA_STREAM_ID = "localStreamPN";

    private PeerConnectionFactory peerConnectionFactory;
    private VideoCapturerAndroid videoCaptureAndroid;
    private VideoSource localVideoSource;
    private AudioSource localAudioSource;
    private MediaStream localMediaStream;
    private int camNumber = VideoCapturerAndroid.getDeviceCount();
    private String frontFacingCam = VideoCapturerAndroid.getNameOfFrontFacingDevice();
    private String backFacingCam  = VideoCapturerAndroid.getNameOfBackFacingDevice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_video);
        boolean check = PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, null);
        if(check){
            peerConnectionFactory = new PeerConnectionFactory();
            videoCaptureAndroid = VideoCapturerAndroid.create(frontFacingCam);

            MediaConstraints videoConstraints = new MediaConstraints();
            localVideoSource = peerConnectionFactory.createVideoSource(videoCaptureAndroid, videoConstraints);
            VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, localVideoSource);

            MediaConstraints audioConstraints = new MediaConstraints();
          //  audioConstraints.mandatory.add();
            localAudioSource = peerConnectionFactory.createAudioSource(audioConstraints);
            AudioTrack localAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, localAudioSource);

            localMediaStream = peerConnectionFactory.createLocalMediaStream("local");
            localMediaStream.addTrack(localVideoTrack);
            localMediaStream.addTrack(localAudioTrack);
        }

    }
}
