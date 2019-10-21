package com.im.topmovie.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.im.topmovie.R
import com.im.topmovie.utils.values.ToastEnum
import kotlinx.android.synthetic.main.custom_toast.view.*

class CustomToast
{
        fun show(context: Context, message: String, status: String)
        {
                val toast = Toast(context)
                when (status)
                {
                        ToastEnum.SUCCESS.value -> toast.success(context, message)
                        ToastEnum.FAILED.value -> toast.failed(context, message)
                        ToastEnum.DEFAULT.value -> toast.default(context, message)
                }
        }

        fun Toast.success(context: Context, message:String){
                val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, (context as AppCompatActivity).findViewById<ViewGroup>(R.id.toast_container))

                layout.toast_container.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                layout.tv_toast.text = message
                layout.tv_toast.setTextColor(ContextCompat.getColor(context, R.color.white))
                layout.iv_toast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check))
                setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
                setDuration(Toast.LENGTH_LONG)
                setView(layout)
                show()
        }

        fun Toast.failed(context: Context, message:String){
                val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, (context as AppCompatActivity).findViewById<ViewGroup>(R.id.toast_container))

                layout.toast_container.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                layout.tv_toast.text = message
                layout.tv_toast.setTextColor(ContextCompat.getColor(context, R.color.white))
                layout.iv_toast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_warning))
                setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
                setDuration(Toast.LENGTH_LONG)
                setView(layout)
                show()
        }

        fun Toast.default(context: Context, message:String){
                val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, (context as AppCompatActivity).findViewById<ViewGroup>(R.id.toast_container))

                layout.toast_container.setBackgroundColor(ContextCompat.getColor(context, R.color.greyLight))
                layout.tv_toast.text = message
                layout.tv_toast.setTextColor(ContextCompat.getColor(context, R.color.blackSoft))
                layout.iv_toast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_warning))
                layout.iv_toast.imageTintList = ContextCompat.getColorStateList(context, R.color.blackSoft)
                setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
                setDuration(Toast.LENGTH_LONG)
                setView(layout)
                show()
        }
}
