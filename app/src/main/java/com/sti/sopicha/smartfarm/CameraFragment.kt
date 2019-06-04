package com.sti.sopicha.smartfarm


import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.MediaController
import android.widget.Toast
import kotlinx.android.synthetic.main.webcam.*
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import android.R.attr.start



class CameraFragment: Fragment(),IVLCVout.Callback, MediaPlayer.EventListener, MediaController.MediaPlayerControl {

    private val TAG = "Test"
    // declare libvlc object
    private var libvlc: LibVLC? = null

    lateinit var cam: WebView
    private var webCamSetting: View? = null

    private var mediaPlayer: MediaPlayer? = null
    private var mcontroller: MediaController? = null
    private var holder: SurfaceHolder? = null // declare surface holder object

    companion object {
        fun newInstance(): CameraFragment = CameraFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        webCamSetting = inflater.inflate(R.layout.webcam, container, false)
        return webCamSetting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        cam = view.findViewById(R.id.videoView)

        cam.loadUrl("http://thuwanon.trueddns.com:59501/videostream.cgi?user=admin&pwd=93759375")
        mediaController = MediaController(context)
        mediaController?.setAnchorView(cam)
        */


        val surfaceView = view.findViewById<SurfaceView>(R.id.surfaceView)

        // get surface view holder to display video
        this.holder = surfaceView!!.holder
        holder!!.setKeepScreenOn(true)

        createPlayer("http://thuwanon.trueddns.com:59501/videostream.cgi?user=admin&pwd=93759375")

    }


    /**
     * Creates MediaPlayer and plays video
     * @param media
     */
    fun createPlayer(media: String) {
        if (mediaPlayer != null && libvlc != null) {
            releasePlayer()
        }
        Log.i(TAG, "Creating vlc player")
        try {
            // create arraylist to assign option to create libvlc object
            val options = ArrayList<String>()
            options.add("--aout=opensles")
            options.add("--http-reconnect")
            options.add("--audio-time-stretch") // time stretching
            options.add("--network-caching=1500")
            options.add("-vvv") // verbosity

            // create libvlc object
            libvlc = LibVLC(activity, options)

            // get surface view holder to display video
            this.holder = surfaceView!!.holder
            holder!!.setKeepScreenOn(true)

            // Creating media player
            mediaPlayer = MediaPlayer(libvlc)

            // Setting up video output
            val vout = mediaPlayer!!.vlcVout
            vout.setVideoView(surfaceView)
            vout.addCallback(this)
            vout.attachViews()

            mcontroller = MediaController(context)
            mcontroller?.setAnchorView(surfaceView)

            val m = Media(libvlc, Uri.parse(media))
            mediaPlayer!!.setMedia(m)
            mediaPlayer!!.play()



        } catch (e: Exception) {
            Toast.makeText(activity, "Error in creating player!", Toast.LENGTH_LONG).show()
        }

    }

        /**
         * release player
         */
        fun releasePlayer() {
            Log.i(TAG, "releasing player started")
            if (libvlc == null)
                return
            mediaPlayer!!.stop()
            var vout: IVLCVout = mediaPlayer!!.vlcVout
            vout.removeCallback(this)
            vout.detachViews()
            mediaPlayer!!.release()
            mediaPlayer = null
            holder = null
            libvlc!!.release()
            libvlc = null
            Log.i(TAG, "released player")
        }

    override fun onEvent(event: MediaPlayer.Event) {

        when (event.type) {
            MediaPlayer.Event.EndReached -> {
                this.releasePlayer()
            }

            MediaPlayer.Event.Playing->Log.i("playing","playing")
            MediaPlayer.Event.Paused->Log.i("paused","paused")
            MediaPlayer.Event.Stopped->Log.i("stopped","stopped")
            else->Log.i("nothing","nothing")
        }
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        val sw = surfaceView!!.width
        val sh = surfaceView!!.height

        if (sw * sh == 0) {
            Log.e(TAG, "Invalid surface size")
            return
        }

        mediaPlayer!!.vlcVout.setWindowSize(sw, sh)
        mediaPlayer!!.aspectRatio="4:3"
        mediaPlayer!!.setScale(0f)
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        releasePlayer()
    }

    //mediacontroller implemented methods

    override fun start() {
        mediaPlayer!!.play()
    }

    override fun pause() {
        mediaPlayer!!.pause()
    }

    override fun getDuration(): Int {
        return 0
    }

    override fun getCurrentPosition(): Int {
        return 0
    }

    override fun seekTo(i: Int) {
        return mediaPlayer!!.navigate(i)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}