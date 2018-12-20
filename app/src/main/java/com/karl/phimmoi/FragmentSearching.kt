package com.karl.phimmoi

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.jsoup.Jsoup

class FragmentSearching : Fragment(), CustomGridViewSearch.OnItemClickListenner {

    private var linkImage: String = ""
    private var linkMovie: String = ""
    private var typeOfMovie: String = ""
    private var nameMovie: String = ""
    private lateinit var dialog: Dialog
    private var updatePhimAdapter: CustomGridViewSearch? = null
    private lateinit var listPhim: ArrayList<Phim>

    companion object {
        private const val URL = "http://goophim.com/"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onItemClicked(phim: Phim) {

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        //val finalLink = link + "xem-phim.html"
        webView2.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        WebStorage.getInstance().deleteAllData()
        webView2.settings.javaScriptEnabled = true
        webView2.settings.domStorageEnabled = true
        webView2.settings.blockNetworkImage = true
        webView2.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView2.settings.loadsImagesAutomatically = false
        webView2.settings.setSupportZoom(false)
        webView2.settings.setGeolocationEnabled(false)
        webView2.addJavascriptInterface(HtmlHandler(context!!, phim.link, activity as MainActivity), "HTMLOUT")

        webView2.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                dialog.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("fuck", "done")
                webView2.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            "var resolution= document.getElementsByClassName('btn btn-success'); " +
                            "for(var i=0;i<resolution.length;i++){ " +
                            "window.HTMLOUT.resolution(resolution[i].textContent); " +
                            "}" +
                            "}) () "
                )
                webView2.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            "var link= document.getElementsByClassName('form-control'); " +
                            "for(var i=0;i<link.length;i++){ " +
                            "window.HTMLOUT.getLink(link[i].getAttribute('value')); " +
                            "}" +
                            "}) () "
                )
                webView2.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var episode = document.getElementsByClassName('btn btn-default'); " +
                            "for(var i=0;i<episode.length;i++){ " +
                            "window.HTMLOUT.getEpisode(episode[i].textContent); " +
                            "}" +
                            "}) () "
                )
                webView2.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var linkEpisode = document.getElementsByClassName('btn btn-default'); " +
                            "for(var i=0;i<linkEpisode.length;i++){ " +
                            "window.HTMLOUT.getLinkEpisode(linkEpisode[i].getAttribute('href')); " +
                            "}" +
                            "}) () "
                )
                webView2.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var result = document.getElementById('vip-1'); " +
                            "window.HTMLOUT.scrollWidth(result.value); " +
                            "}) () "
                )

                webView2.loadUrl(
                    "javascript:( " +
                            "function() { " +
                            "var result=document.getElementsByClassName('btn btn-danger')[0].textContent;" +
                            "window.HTMLOUT.checkLinkAvailable(result);" +
                            "}) ()"
                )

                dialog.dismiss()


            }
        }
        webView2.loadUrl(URL + phim.link)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(context!!)
        listPhim = ArrayList()
        val bundle = arguments ?: return
        val link = bundle.getString("link")
        val finalType = bundle.getString("typeOfServer")
        val task = TaskSearch()
        task.execute(link)
    }


    inner class TaskSearch : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }

        override fun doInBackground(vararg p0: String?): String {
            val document = Jsoup.connect(p0[0]).get()
            val elements = document.select("article.col-item")
            Log.d("sizz", elements.size.toString())
            for (element in elements) {
                val image = element.getElementsByTag("img")
                linkImage = image.attr("data-src")
                val movie = element.getElementsByTag("a")
                linkMovie = movie.attr("href")
                nameMovie = image.attr("alt")
                if (linkMovie == "PBH") {
                    listPhim.add(Phim("https:$linkImage", nameMovie, linkMovie))
                } else if (linkMovie == "BL") {
                    listPhim.add(Phim("https:$linkImage", nameMovie, linkMovie))
                } else {
                    listPhim.add(Phim(linkImage, nameMovie, linkMovie))
                }

            }

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dialog.dismiss()
            Log.d("fick", listPhim.size.toString())
            if (updatePhimAdapter == null) {
                updatePhimAdapter = CustomGridViewSearch(listPhim, context!!)
                updatePhimAdapter?.setOnItemClickListenner(this@FragmentSearching)
                rcvSearching.adapter = updatePhimAdapter
            } else {
                updatePhimAdapter?.notifyDataSetChanged()
            }
        }

    }

}