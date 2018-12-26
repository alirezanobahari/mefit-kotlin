package ir.softap.mefit.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import ir.softap.mefit.utilities.Timber
import ir.softap.mefit.utilities.extensions.TAG

private const val RPG_NUMBER = "308243"

class SmsBroadcastReceiver(private val onReceive: (String) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            intent?.extras?.apply {
                val pdus = get("pdus") as Array<Any>
                try {
                    for (i in 0..pdus.size) {
                        val message = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        if (message.originatingAddress?.contains(RPG_NUMBER) == true) {
                            message.messageBody.split("\r\n")[0].split(":").let {
                                if (it[0].contains("کد فعالسازی")) {
                                    onReceive(it[1].substring(0, 4))
                                    return
                                }
                            }
                        } else
                            break
                    }
                } catch (e: Exception) {
                    Timber.e { "${TAG()}, $e" }
                }
            }
        }
    }

}