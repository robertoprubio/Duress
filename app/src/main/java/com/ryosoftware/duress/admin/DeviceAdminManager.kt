package com.ryosoftware.duress.admin

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ryosoftware.duress.Preferences

class DeviceAdminManager(private val ctx: Context) {
    private val dpm = ctx.getSystemService(DevicePolicyManager::class.java)
    private val deviceAdmin by lazy { ComponentName(ctx, DeviceAdminReceiver::class.java) }

    fun remove() = dpm?.removeActiveAdmin(deviceAdmin)
    fun isActive() = dpm?.isAdminActive(deviceAdmin) ?: false

    fun wipeData() {
        var flags = 0
        if (Preferences.new(ctx).isWipeEmbeddedSim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            flags = flags.or(DevicePolicyManager.WIPE_EUICC)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            flags = flags.or(DevicePolicyManager.WIPE_SILENTLY)
        dpm?.wipeData(flags)
    }

    fun makeRequestIntent() =
        Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
}