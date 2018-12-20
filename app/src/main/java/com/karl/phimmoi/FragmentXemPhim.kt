package com.karl.phimmoi

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_left.*
import kotlinx.android.synthetic.main.fragment_xem_phim.*
import org.jsoup.Jsoup
import org.w3c.dom.Document
import org.w3c.dom.Text

class FragmentXemPhim : Fragment() {
    private var finalSrc: String = ""
    private var tempLink = ""
    private lateinit var dialog: Dialog
    private var currentPosition: Int = 0
    private var iOnSendBackTime: IOnSendBackTime? = null
    private var isFull = false
    private var title: String = ""
    private var linkMovie: String = ""


    companion object {
        const val TAG = "FragmentXemphim"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        iOnSendBackTime = context as IOnSendBackTime
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_xem_phim, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = com.karl.phimmoi.Dialog(context!!)
        //view.isFocusableInTouchMode = true
        val bundle: Bundle = arguments ?: return
        linkMovie = bundle.getString("linkPhim")
        title = bundle.getString("title")
        val listReso = bundle.getStringArrayList("listReso")
        val listLinks = bundle.getStringArrayList("listLinks")
        val listEpisodes = bundle.getStringArrayList("listEpisodes")
        val listLinkE = bundle.getStringArrayList("listLinkE")
        val listServers = bundle.getStringArrayList("listServer")
        val textViews = arrayOfNulls<TextView>(listReso.size)
        val textViews2 = arrayOfNulls<TextView>(listEpisodes.size)
        val textViews3 = arrayOfNulls<TextView>(listServers.size)
        videoView.setVideoURI(Uri.parse(linkMovie))
        videoView.setVideoViewCallback(object : UniversalVideoView.VideoViewCallback {
            override fun onBufferingStart(mediaPlayer: MediaPlayer?) {

            }

            override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {

            }

            override fun onPause(mediaPlayer: MediaPlayer?) {

            }

            override fun onScaleChange(isFullscreen: Boolean) {
                if (isFullscreen) {
                    isFull = true
                    reso.visibility = View.GONE
                    layoutReso.visibility = View.GONE
//                    if (activity?.drawer_layout != null) {
//                        activity!!.drawer_layout.visibility = View.GONE
//                    }
                    if (activity?.appbar != null) {
                        activity!!.appbar.visibility = View.GONE
                    }
                    layoutXemPhim.setPadding(0, 0, 0, 0)
                    Toast.makeText(context, "fullscreen", Toast.LENGTH_SHORT).show()
                    currentPosition = videoView.currentPosition
                    videoView.pause()
                    val layoutParams = video_layout.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    video_layout.layoutParams = layoutParams
                    videoView.seekTo(currentPosition)
                    videoView.start()

                } else {
                    isFull = false
                    layoutXemPhim.setPadding(0, 52, 0, 0)
                    reso.visibility = View.VISIBLE
                    layoutReso.visibility = View.VISIBLE
//                    if (activity?.drawer_layout != null) {
//                        activity!!.drawer_layout.visibility = View.VISIBLE
//                    }
                    if (activity?.appbar != null) {
                        activity!!.appbar.visibility = View.VISIBLE
                    }

                    Toast.makeText(context, "wrapcontent", Toast.LENGTH_SHORT).show()
                    currentPosition = videoView.currentPosition
                    videoView.pause()
                    val layoutParams = video_layout.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    video_layout.layoutParams = layoutParams
                    videoView.seekTo(currentPosition)
                    videoView.start()

                }
            }

            override fun onStart(mediaPlayer: MediaPlayer?) {


            }

        })
        videoView.setMediaController(media_controller)
        media_controller.setTitle(title)
        videoView.start()


        for (i in 0 until listReso.size) {
            textViews[i] = TextView(context)

            textViews[i]?.text = listReso[i]
            textViews[i]?.setPadding(4, 0, 4, 0)
            textViews[i]?.setOnClickListener {
                videoView.setVideoPath(listLinks[i])
                videoView.start()
                Toast.makeText(context, "Resolution changed to " + listReso[i], Toast.LENGTH_SHORT).show()
                textViews[i]?.setTextColor(Color.parseColor("#EE0000"))
            }
            if (textViews[i]?.parent != null) {
                val viewGroup: ViewGroup = textViews[i]?.parent as ViewGroup
                viewGroup.removeView(textViews[i])
            }

            layoutReso.addView(textViews[i])
        }
        Log.d("justfck", listLinkE.size.toString())
        Log.d("justfckS", listServers.size.toString())
        for (i in 0 until listEpisodes.size) {
            Log.d("justfck", "episode:  " + listEpisodes[i])
        }
        for (i in 0 until listServers.size) {
            textViews3[i] = TextView(context)

            textViews3[i]?.text = listServers[i]
            textViews3[i]?.setPadding(4, 0, 4, 0)
            if (textViews3[i]?.parent != null) {
                val viewGroup: ViewGroup = textViews3[i]?.parent as ViewGroup
                viewGroup.removeView(textViews3[i])
            }

            layoutServer.addView(textViews3[i])
        }
        for (i in 0 until listEpisodes.size) {
            textViews2[i] = TextView(context)

            textViews2[i]?.text = listEpisodes[i]
            textViews2[i]?.setPadding(4, 0, 4, 0)
            textViews2[i]?.setOnClickListener {

                Toast.makeText(context, "Just wait you are moving to " + listEpisodes[i], Toast.LENGTH_SHORT)
                    .show()
                textViews2[i]?.setTextColor(Color.parseColor("#EE0000"))
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                )
                webView3.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                WebStorage.getInstance().deleteAllData()
                webView3.settings.javaScriptEnabled = true
                webView3.settings.domStorageEnabled = true
                webView3.settings.blockNetworkImage = true
                webView3.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webView3.settings.loadsImagesAutomatically = false
                webView3.settings.setSupportZoom(false)
                webView3.settings.setGeolocationEnabled(false)
                webView3.addJavascriptInterface(
                    HtmlHandler(context!!, listLinkE[i], activity as MainActivity),
                    "HTMLOUT"
                )

                webView3.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        dialog.show()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("fuck", "done")
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    "var resolution= document.getElementsByClassName('btn btn-success'); " +
                                    "for(var i=0;i<resolution.length;i++){ " +
                                    "window.HTMLOUT.resolution(resolution[i].textContent); " +
                                    "}" +
                                    "}) () "
                        )
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    "var link= document.getElementsByClassName('form-control'); " +
                                    "for(var i=0;i<link.length;i++){ " +
                                    "window.HTMLOUT.getLink(link[i].getAttribute('value')); " +
                                    "}" +
                                    "}) () "
                        )
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    " var episode = document.getElementsByClassName('btn-group col-md-12')[2].textContent; " +
                                    "window.HTMLOUT.getEpisode(episode); " +
                                    "}) () "
                        )
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    " var server = document.getElementsByClassName('btn-group col-md-12')[1].textContent; " +
                                    "window.HTMLOUT.getServer(server); " +
                                    "}) () "
                        )
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    " var linkEpisode = document.getElementsByClassName('btn btn-default'); " +
                                    "for(var i=0;i<linkEpisode.length;i++){ " +
                                    "window.HTMLOUT.getLinkEpisode(linkEpisode[i].getAttribute('href')); " +
                                    "}" +
                                    "}) () "
                        )

                        webView3.loadUrl(
                            "javascript:( " +
                                    "function () {" +
                                    " var result = document.getElementById('vip-1'); " +
                                    "window.HTMLOUT.scrollWidth(result.value); " +
                                    "}) () "
                        )
                        webView3.loadUrl(
                            "javascript:( " +
                                    "function() { " +
                                    "var result=document.getElementsByClassName('btn btn-danger')[0].textContent;" +
                                    "window.HTMLOUT.checkLinkAvailable(result);" +
                                    "}) ()"
                        )

                        dialog.dismiss()

                    }
                }
                webView3.loadUrl(listLinkE[i])
            }
            if (textViews2[i]?.parent != null) {
                val viewGroup: ViewGroup = textViews2[i]?.parent as ViewGroup
                viewGroup.removeView(textViews2[i])
            }

            layoutEpisode.addView(textViews2[i])

        }
        for (i in 0 until listLinkE.size) {
            Log.d("justfck", listLinkE[i])
        }

    }

    interface IOnSendBackTime {
        fun onTimeVideo(time: Int, linkMovie: String, isFullscreen: Boolean)
    }

    fun setOnBackTime(iOnSendBackTime: IOnSendBackTime) {
        this.iOnSendBackTime = iOnSendBackTime
    }

    override fun onStop() {
        Log.d("xemp", "on Stop....")
        videoView.pause()
        currentPosition = videoView.currentPosition
        iOnSendBackTime?.onTimeVideo(currentPosition, linkMovie, isFull)
        super.onStop()

    }


}