package com.karl.phimmoi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface

class HtmlHandler(private val context: Context, private val url: String, private val activity: MainActivity) {
    private val listResolutions = ArrayList<String>()
    private var title: String = ""
    private val listLinkFilms = ArrayList<String>()
    private val listEpisodes = ArrayList<String>()
    private val listServers = ArrayList<String>()
    private val listSaveTrash = ArrayList<String>()
    private val listLinkE = ArrayList<String>()
    @JavascriptInterface
    fun scrollWidth(html: String) {
        val fragmentXemPhim = FragmentXemPhim()
        val bundle = Bundle()
        bundle.putString("linkPhim", html)
        bundle.putString("title", title)
        bundle.putStringArrayList("listReso", listResolutions)
        bundle.putStringArrayList("listLinks", listLinkFilms)
        bundle.putStringArrayList("listEpisodes", listEpisodes)
        bundle.putStringArrayList("listLinkE", listLinkE)
        bundle.putStringArrayList("listServer", listServers)
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.layoutFather, fragmentXemPhim, "Watch")
            .addToBackStack(null)
            .commit()
        fragmentXemPhim.arguments = bundle
    }

    @JavascriptInterface
    fun checkLinkAvailable(link: String?) {
        if (link != "") {
            Log.d("justtest", link)
            val dialog = BottomDialogReload(context, url)
            dialog.show()
        }
    }

    @JavascriptInterface
    fun getTitle(title: String) {
        this.title = title
    }

    @JavascriptInterface
    fun resolution(reso: String) {
        if (!listResolutions.contains(reso) && reso[0] <= '9' && reso[0] >= '0') {
            listResolutions.add(reso)
        }


    }

    @JavascriptInterface
    fun getLink(link: String?) {
        if (link != null && link != "") {
            listLinkFilms.add(link)
            Log.d("linkMovie", link)
        }

    }

    @JavascriptInterface
    fun getEpisode(episode: String?) {
        if (episode != null && episode != "") {
            if (!listEpisodes.contains(episode)) {
                listEpisodes.add(episode)
            }
        }


    }

    @JavascriptInterface
    fun getServer(server: String?) {
        if (server != null) {
            if (!listServers.contains(server)) {
                listServers.add(server)
            }
        }


    }

    @JavascriptInterface
    fun getLinkEpisode(link: String?) {
        if (link != null && link != "") {
            if (!listLinkE.contains("http://goophim.com/$link")) {
                listLinkE.add("http://goophim.com/$link")
                //Log.d("testzzz", "http://goophim.com/$link")
            }


        }
        for (i in 0 until listLinkE.size) {
            checkExists(listLinkE[i])
        }

    }

    fun checkExists(result: String) {

        var count = 1
        var count2 = 0
        val url = "http://goophim.com/"
        for (i in 0 until result.length) {
            if (url[0] == result[i]) {
                for (j in 1 until url.length) {
                    if (result[i + j] == url[j]) {
                        count++
                    } else if (count == url.length) {
                        count2++
                        count = 1
                    } else {
                        break
                    }
                }

            }
        }
        if (count2 > 1 && !listSaveTrash.contains(result)) {
            listSaveTrash.add(result)
        }

        for (i in 0 until listLinkE.size) {
            for (j in 0 until listSaveTrash.size) {
                if (listLinkE[i] == listSaveTrash[j]) {
                    listLinkE.remove(listLinkE[i])
                }
            }
        }

    }


}