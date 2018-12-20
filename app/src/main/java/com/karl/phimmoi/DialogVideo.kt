package com.karl.phimmoi

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_video.*
import kotlinx.android.synthetic.main.fragment_xem_phim.*

class DialogVideo : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linkMovie = arguments!!.getString("linkMovie")

        val time = arguments!!.getInt("time")
        Log.d("time", "time: $time/ $linkMovie")
        videoDialog.setVideoPath(linkMovie)
        videoDialog.seekTo(time)
        videoDialog.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_video, container, false)

    }
}