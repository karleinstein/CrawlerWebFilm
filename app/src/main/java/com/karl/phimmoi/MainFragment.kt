package com.karl.phimmoi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_left.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_xem_phim.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MainFragment : Fragment(), UpdatePhimAdapter.OnItemClickListenner {
    private lateinit var imageLink: StringBuffer
    private var listPhim1: ArrayList<Phim>? = null
    private var listPhim2: ArrayList<Phim>? = null
    private var listPhim3: ArrayList<Phim>? = null
    private var listPhim4: ArrayList<Phim>? = null
    private var listPhim5: ArrayList<Phim>? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private var updatePhimAdapter1: UpdatePhimAdapter? = null
    private var updatePhimAdapter2: UpdatePhimAdapter? = null
    private var updatePhimAdapter3: UpdatePhimAdapter? = null
    private var updatePhimAdapter5: UpdatePhimAdapter? = null
    private var linkMovieFinal: String? = null
    private var task1: Task1? = null
    private var task2: Task2? = null
    private var task3: Task3? = null
    private var task4: Task4? = null
    private var task5: Task5? = null
    private var updatePhimAdapter4: UpdatePhimAdapter? = null
    private var link: String = ""
    private lateinit var dialog: Dialog
    private var typeOfMovide1: String = ""
    private var typeOfMovide2: String = ""
    private var typeOfMovide3: String = ""
    private var typeOfMovide4: String = ""
    private var typeOfMovide5: String = ""
    private var finalType: String = "PM"
    private var url: String = "http://goophim.com/PM.html"
    private var signal: Int = 1

    private fun restartTask() {
        if (task1?.status == AsyncTask.Status.RUNNING &&
            task2?.status == AsyncTask.Status.RUNNING
            && task3?.status == AsyncTask.Status.RUNNING
            && task4?.status == AsyncTask.Status.RUNNING
            && task5?.status == AsyncTask.Status.RUNNING
        ) {

            task1?.cancel(true)
            task2?.cancel(true)
            task3?.cancel(true)
            task4?.cancel(true)
            task5?.cancel(true)

        }


    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webViewLoadUrl(url: String) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        //val finalLink = link + "xem-phim.html"
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        WebStorage.getInstance().deleteAllData()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.blockNetworkImage = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.loadsImagesAutomatically = false
        webView.settings.setSupportZoom(false)
        webView.settings.setGeolocationEnabled(false)
        webView.addJavascriptInterface(HtmlHandler(context!!, url, activity as MainActivity), "HTMLOUT")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                dialog.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("fuck", "done")
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            "var resolution= document.getElementsByClassName('btn btn-success'); " +
                            "for(var i=0;i<resolution.length;i++){ " +
                            "window.HTMLOUT.resolution(resolution[i].textContent); " +
                            "}" +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            "var link= document.getElementsByClassName('form-control'); " +
                            "for(var i=1;i<link.length;i++){ " +
                            "window.HTMLOUT.getLink(link[i].value); " +
                            "}" +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var episode = document.getElementsByClassName('btn-group col-md-12')[2].textContent; " +
                            "window.HTMLOUT.getEpisode(episode); " +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var server = document.getElementsByClassName('btn-group col-md-12')[1].textContent; " +
                            "window.HTMLOUT.getServer(server); " +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var linkEpisode = document.getElementsByClassName('btn btn-default'); " +
                            "for(var i=0;i<linkEpisode.length;i++){ " +
                            "window.HTMLOUT.getLinkEpisode(linkEpisode[i].getAttribute('href')); " +
                            "}" +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var result = document.title; " +
                            "window.HTMLOUT.getTitle(result); " +
                            "}) () "
                )
                webView.loadUrl(
                    "javascript:( " +
                            "function () {" +
                            " var result = document.getElementById('vip-1').value; " +
                            "window.HTMLOUT.scrollWidth(result); " +
                            "}) () "
                )
//                webView.loadUrl(
//                    "javascript:( " +
//                            "function() { " +
//                            "var result=document.getElementsByClassName('btn btn-danger')[0].textContent;" +
//                            "window.HTMLOUT.checkLinkAvailable(result);" +
//                            "}) ()"
//                )


                dialog.dismiss()


            }
        }
        webView.loadUrl(url)

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onItemClicked(phim: Phim) {
        link = phim.link
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        webViewLoadUrl(link)

    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeface = Typeface.createFromAsset(activity?.assets, "font/diablo_h.ttf")
        val beyno = Typeface.createFromAsset(activity?.assets, "font/BEYNO.ttf")
        dialog = com.karl.phimmoi.Dialog(context!!)

        typeOfMovide5 = "phimhot"
        finalType = arguments!!.getString("finalType")
        url = arguments!!.getString("url")
        Log.d("cac", finalType)
        signal = arguments!!.getInt("signal")
        Log.d("cac", url)
        txtPhimBoMoiUp.typeface = typeface
        txtPhimLeMoiUp.typeface = typeface
        txtPhimHHMoiUp.typeface = typeface
        txtPhimChieuRap.typeface = typeface
        txtPhimHot.typeface = typeface
        listPhim1 = ArrayList()
        listPhim2 = ArrayList()
        listPhim3 = ArrayList()
        listPhim4 = ArrayList()
        listPhim5 = ArrayList()
        imageLink = StringBuffer()
        task5 = null
        task5 = Task5()
        task5?.execute(url)
        typeOfMovide5 = "phimhot"
        if (finalType == "TVHay") {
            typeOfMovide1 = "phimmoi"
            task1 = null
            task1 = Task1()
            task1?.execute(url)
            typeOfMovide2 = "phimle"
            task2 = null
            task2 = Task2()
            task2?.execute(url)
            typeOfMovide3 = "phimbo"
            task3 = null
            task3 = Task3()
            task3?.execute(url)
            typeOfMovide4 = "phimhoathinh"
            task4 = null
            task4 = Task4()
            task4?.execute(url)
        } else {
            typeOfMovide1 = "phimchieurap"
            task1 = null
            task1 = Task1()
            task1?.execute(url)
            typeOfMovide2 = "phimlemoi"
            task2 = null
            task2 = Task2()
            task2?.execute(url)
            typeOfMovide3 = "phimbomoi"
            task3 = null
            task3 = Task3()
            task3?.execute(url)
            typeOfMovide4 = "phimhoathinh"
            task4 = null
            task4 = Task4()
            task4?.execute(url)
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.d("cac", "on Destroy View....")
//        //restartTask()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("cac", "on Destroy...")
//    }

    override fun onDetach() {
        super.onDetach()
        Log.d("cac", "on detach....")

        restartTask()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("cac", "on attach.....")
        updatePhimAdapter1 = null
        updatePhimAdapter2 = null
        updatePhimAdapter3 = null
        updatePhimAdapter4 = null
        updatePhimAdapter5 = null
        listPhim1 = null
        listPhim2 = null
        listPhim3 = null
        listPhim4 = null
        listPhim5 = null
        //restartTask()
    }

    private fun checkWtf(url: String?, listPhim: ArrayList<Phim>, theFuck: String) {
        val document: Document = Jsoup.connect(url)
            .header("Cookie", "ZvcurrentVolume=100; zvAuth=1; zvLang=0; ZvcurrentVolume=100; notice=11")
            .get()
        val elements: Elements = document.select("div#$theFuck>div.row>div")
        for (element in elements) {
            val linkE = element.getElementsByTag("a")
            link = linkE.attr("href")
            val imageE = element.getElementsByTag("img")
            val image = imageE.attr("data-src")
            val vnName = imageE.attr("alt")
            Log.d("just", "$vnName  $image  $link")
            var size = image.length - 3
//                var i=size
//                while (size > 21) {
//                    imageLink.append(image[size])
//                    size--
//                }
//                imageLink.reverse()
            // Log.d(TAG, imageLink.toString())

            var imageLink: String = ""
            if (finalType == "PBH") {
                imageLink = "https:$image"
            } else if (finalType == "BL") {
                imageLink = "https:$image"
            } else {
                imageLink = image
            }
            listPhim.add(Phim(imageLink, vnName, "http://goophim.com/$link"))
            Log.d("thefuck", link)
            link = ""
            //val temp =
            //  listPhim1.size.toString() + "  " + listPhim2.size.toString() + " " + listPhim3.size.toString() + "  " + listPhim4.size.toString()


        }
    }

    inner class Task1 : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            checkWtf(p0[0], listPhim1!!, typeOfMovide1)
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            setAdapter(updatePhimAdapter1, rcvPhimChieuRap, listPhim1!!)
        }


    }

    inner class Task2 : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            checkWtf(p0[0], listPhim2!!, typeOfMovide2)
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            setAdapter(updatePhimAdapter2, rcvPhimLeMoiUp, listPhim2!!)
        }


    }

    inner class Task3 : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            checkWtf(p0[0], listPhim3!!, typeOfMovide3)
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            setAdapter(updatePhimAdapter3, rcvPhimBoMoiUp, listPhim3!!)
        }


    }

    inner class Task4 : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            checkWtf(p0[0], listPhim4!!, typeOfMovide4)
            return ""
        }


        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dialog.dismiss()
            setAdapter(updatePhimAdapter4, rcvPhimHHMoiUp, listPhim4!!)
        }


    }

    inner class Task5 : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            checkWtf(p0[0], listPhim5!!, typeOfMovide5)
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            setAdapter(updatePhimAdapter5, rcvPhimHot, listPhim5!!)
        }

    }


    private fun setAdapter(
        updatePhimAdapter: UpdatePhimAdapter?,
        recyclerView: RecyclerView,
        listPhim: ArrayList<Phim>
    ) {
        var updatePhimAdapterT = updatePhimAdapter
        if (updatePhimAdapterT == null) {
            //Log.d(TAG, listPhim[0].imageLink)
            updatePhimAdapterT = UpdatePhimAdapter(listPhim, context!!)
            updatePhimAdapterT.setOnItemClickListenner(this@MainFragment)
            recyclerView.layoutManager =
                    LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = updatePhimAdapterT

        } else {

            updatePhimAdapterT.notifyDataSetChanged()
        }
    }
}