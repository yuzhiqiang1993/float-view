package com.yzq.floating_view

import android.app.Activity
import android.view.View
import android.view.ViewGroup


internal fun Activity.rootContainer(): ViewGroup? {
    return runCatching {
        return this.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
    }.getOrNull()
}
