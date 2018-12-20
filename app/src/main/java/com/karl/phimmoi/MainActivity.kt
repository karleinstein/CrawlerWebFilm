package com.karl.phimmoi

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_left.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_xem_phim.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.apache.xpath.operations.Bool
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
    , FragmentXemPhim.IOnSendBackTime {


    private var finalType: String = "PM"
    private var linkMovie: String = ""
    private var isFull = false
    private var url_search = "http://goophim.com/tim-kiem/PBH/"
    private val fragmentMain = MainFragment()
    private var isBack = false
    private var time: Int = 0
    private var url: String = "http://goophim.com/PM.html"
    override fun onTimeVideo(time: Int, linkMovie: String, isFull: Boolean) {
        this.isFull = isFull
        Log.d("time", linkMovie.toString())
        if (isBack) {
            val dialogVideo = DialogVideo()
            supportFragmentManager.beginTransaction()
                .add(R.id.layoutFather, dialogVideo)
                .commit()
            val bundle = Bundle()
            bundle.putInt("time", time)
            bundle.putString("linkMovie", linkMovie)
            dialogVideo.arguments = bundle
            Log.d("trash", linkMovie)
            isBack = false
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSearch -> {
                if (edtFind.text.isNotEmpty()) {
                    val temp: String = edtFind.text.toString()
                    val temp2 = temp.toLowerCase().trim()
                    var ja = ""
                    for (i in 0 until temp2.length) {

                        ja += if (temp2[i] != ' ') {
                            temp2[i]
                        } else {
                            "%20"
                        }
                    }
                    Log.d("teszz", ja)
                    when (finalType) {
                        "PM" -> {
                            url_search = "http://goophim.com/tim-kiem/PM/"
                            //txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "PBH" -> {
                            url_search = "http://goophim.com/tim-kiem/PBH/"
                            // txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "PN" -> {
                            url_search = "http://goophim.com/tim-kiem/phimnhanh/"
                            // txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "P14" -> {
                            url_search = "http://goophim.com/tim-kiem/P14/"
                            // txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "BL" -> {
                            url_search = "http://goophim.com/tim-kiem/BL/"
                            // txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "HDO" -> {
                            url_search = "http://goophim.com/tim-kiem/HDO/"
                            //txtPhimHHMoiUp.visibility = View.VISIBLE
                        }
                        "TVHay" -> {
                            url_search = "http://goophim.com/tim-kiem/tvHay/"
                            // txtPhimHHMoiUp.visibility = View.GONE
                        }

                    }
                    val link = "$url_search$temp/page-1.html"
                    val bundle = Bundle()
                    val fragmentSearching = FragmentSearching()
                    bundle.putString("link", link)
                    bundle.putString("typeOfServer", finalType)
                    supportFragmentManager.beginTransaction()
                        .add(R.id.layoutFather, fragmentSearching)
                        .commit()
                    fragmentSearching.arguments = bundle
                } else {
                    Toast.makeText(this, "Please enter name of movie you want to search", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.ServerPhimBatHu -> {
                url = "http://goophim.com/PBH.html"
                finalType = "PBH"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
            R.id.ServerPhimmoi -> {
                url = "http://goophim.com/PM.html"
                finalType = "PM"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
            R.id.ServerPhim14 -> {
                url = "http://goophim.com/P14.html"
                finalType = "P14"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
            R.id.ServerBiluTV -> {
                url = "http://goophim.com/BL.html"
                finalType = "BL"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
            R.id.ServerHdo -> {
                url = "http://goophim.com/HDO.html"
                finalType = "HDO"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
            R.id.ServerPhimnhanh -> {
                url = "http://goophim.com/phimnhanh.html"
                finalType = "PN"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)

            }
            R.id.ServerTVHay -> {
                url = "http://goophim.com/tvhay.html"
                finalType = "TVHay"
                sendingTypeServer(finalType, url)
                drawer_layout.closeDrawer(Gravity.START, false)
            }
        }
        return true
    }

    private fun sendingTypeServer(finalType: String, url: String) {
        supportFragmentManager.beginTransaction()
            .remove(fragmentMain)
            .add(R.id.layoutFather, fragmentMain, "Main")
            .addToBackStack(null)
            .commit()
        val bundle = Bundle()
        bundle.putInt("signal", 2)
        bundle.putString("finalType", finalType)
        bundle.putString("url", url)
        fragmentMain.arguments = bundle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_left)
        val fragmentXemPhim = FragmentXemPhim()
        fragmentXemPhim.setOnBackTime(this)
        nav_view.setNavigationItemSelectedListener(this)
        btnSearch.setOnClickListener(this)
        val typeface = Typeface.createFromAsset(assets, "font/diablo_h.ttf")
        val beyno = Typeface.createFromAsset(assets, "font/BEYNO.ttf")
        val headerView = nav_view.getHeaderView(0)
        headerView.txtEzMovie.typeface = typeface
        headerView.txtEzMovie2.typeface = beyno
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setSupportActionBar(toolbar)
        val fragmentMain = MainFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutFather, fragmentMain, "Main")
            .addToBackStack(null)
            .commit()
        val bundle = Bundle()
        bundle.putString("finalType", "PM")
        bundle.putInt("signal", 1)
        bundle.putString("url", url)
        fragmentMain.arguments = bundle
    }

    override fun onBackPressed() {

        Log.d("back", "is back?...")
        val fragmentWatchMovie = supportFragmentManager.findFragmentByTag("Watch")
        if (fragmentWatchMovie != null && !isFull) {
            Log.d("back", "nice")
            isBack = true
            super.onBackPressed()

        }
    }
}
