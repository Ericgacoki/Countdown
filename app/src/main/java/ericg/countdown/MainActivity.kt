package ericg.countdown

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat.getTimeInstance
import java.util.*

class MainActivity : AppCompatActivity() {

    private var clickCounter = 0
    private val CHANNEL_ID = "channel1_ID"
    private var notify_ID = 0
    private var notificationText1 = ""
    private var importanceType = NotificationManager.IMPORTANCE_HIGH
    private var priorityType = NotificationCompat.PRIORITY_HIGH


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        priority_bell.setImageDrawable(getDrawable(R.drawable.ic_notifications_on))

        btn_share.visibility = View.INVISIBLE
        edit.isEnabled = false

        clicksHandler()
        updateUI()
        createNotificationChannel()

    }

    private fun generateRandomNumber(start: Int, end: Int) = (start..end).shuffled().last()

    private fun updateUI() {

        btn_start.setOnClickListener {

            if (name.length() <= 5 || name.length() <= 0) {
                name.error = " Enter full name "
            } else {
                clickCounter += 1
                btn_share.visibility = View.VISIBLE
                edit.isEnabled = true

                if (clickCounter > 2) {

                    btn_start.visibility = View.INVISIBLE
                    btn_start.isEnabled = false

                }
                name.visibility = View.INVISIBLE
                updatedName.text = name.text.toString()
                updatedName.append("'s")

                //Generate a random time
                val years = generateRandomNumber(0, 75)
                num_years.text = years.toString()
                val months = generateRandomNumber(0, 11)
                num_months.text = months.toString()
                val days = generateRandomNumber(0, 29)
                num_days.text = days.toString()
                val hours = generateRandomNumber(0, 23)
                num_hours.text = hours.toString()

                num_minutes.text = ("--:--")
                num_seconds.text = ("--:--")

                //This text will be used as the ContentText in a notification

                notificationText1 = if (years <= 5) {

                    num_years.setTextColor(getColor(R.color.colorRed))
                    num_months.setTextColor(getColor(R.color.colorRed))
                    num_days.setTextColor(getColor(R.color.colorRed))

                    "Prepare your grave as early as now!."

                } else {

                    num_years.setTextColor(getColor(R.color.colorWhite))
                    num_months.setTextColor(getColor(R.color.colorWhite))
                    num_days.setTextColor(getColor(R.color.colorWhite))

                    "You have $years years to Enjoy."
                }
                //show Notification

                val time = getTimeInstance().format(Calendar.getInstance().time)
                deathNotification(
                   "Remaining Time as from $time",
                notificationText1,
                   priorityType,
                    0,
                    0,
                    false
                )
            }
        }
        btn_share.setOnClickListener {
            //TODO("Share a screenshot of the current Activity")
            Toast.makeText(this, " Share to WhatsApp !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clicksHandler() {
        var on = true
        priority_bell.setOnClickListener {

             on = if(on){
                 priority_bell.setImageDrawable(getDrawable(R.drawable.ic_notifications_off))

                importanceType = NotificationManager.IMPORTANCE_DEFAULT
                priorityType = NotificationCompat.PRIORITY_DEFAULT
                Toast.makeText(this, " Default Priority & Low Importance engaged  ", Toast.LENGTH_SHORT).show()

                 false
        }
            else{
                 priority_bell.setImageDrawable(getDrawable(R.drawable.ic_notifications_on))

                 importanceType = NotificationManager.IMPORTANCE_HIGH
                 priorityType = NotificationCompat.PRIORITY_HIGH
                 Toast.makeText(this, " High Priority & High Importance engaged  ", Toast.LENGTH_SHORT).show()

                 true
             }
    }
        edit.setOnClickListener {
            Toast.makeText(this, "Only for Premium users!", Toast.LENGTH_SHORT).show()

            val upgradeDialog = AlertDialog.Builder(this, 0)
            upgradeDialog.setIcon(getDrawable(R.drawable.ic_upgrade))
            upgradeDialog.setTitle("Upgrade to Premium")
            upgradeDialog.setMessage("You must upgrade to Premium to ENABLE this feature!")
            upgradeDialog.setPositiveButtonIcon(getDrawable(R.drawable.ic_download))
            upgradeDialog.setPositiveButton(" ") { _, _ ->

                deathNotification(
                    "Downloading",
                    "This might take some time",
                    priorityType,
                    0,
                    100,
                    true
                )

            }
            upgradeDialog.setNegativeButtonIcon(getDrawable(R.drawable.ic_cancel))
            upgradeDialog.setNegativeButton("                                ") { _, _ ->

                // create Another Alert dialog and notification

                if (name.visibility == View.INVISIBLE) {

                    val brokenAgreement = AlertDialog.Builder(this, 0)
                    brokenAgreement.setIcon(getDrawable(R.drawable.ic_cancel))
                    brokenAgreement.setTitle("DEATH COUNTDOWN")
                    brokenAgreement.setMessage("             USER   AGREEMENT    BROKEN !")
                    brokenAgreement.setCancelable(false)
                    brokenAgreement.show()

                    // set Time to 2 min and colors to red
                    num_years.text = ("00")
                    num_months.text = ("00")
                    num_days.text = ("00")
                    num_hours.text = ("00")
                    num_minutes.text = ("01")
                    num_seconds.text = ("59")

                    notificationText1 = "Watch yourself die..."
                    deathNotification(
                        " ALMOST DEAD ! ", notificationText1,
                        priorityType,
                        0,
                        0,
                        false
                    )

                    num_years.setTextColor(getColor(R.color.colorRed))
                    num_months.setTextColor(getColor(R.color.colorRed))
                    num_days.setTextColor(getColor(R.color.colorRed))
                    num_hours.setTextColor(getColor(R.color.colorRed))
                    num_minutes.setTextColor(getColor(R.color.colorRed))
                    num_seconds.setTextColor(getColor(R.color.colorRed))

                    deathNotification(
                        "USER AGREEMENT BROKEN",
                        "${name.text} has violated Terms of service",
                        priorityType,
                        0,
                        0,
                        false
                    )

                }
            }
            upgradeDialog.setCancelable(false)
            upgradeDialog.show()
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val descriptionText = "Death time!"
            val channel_1 = NotificationChannel(
                CHANNEL_ID,
                "Death notification",
                importanceType
            ).apply {
                description = descriptionText
            }

            //Register this channel
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel_1)

        }
    }

    private fun deathNotification(
        contentTitle: String,
        contentText: String,
        priorityType:Int,
        progressCurrent: Int,
        progressEnd: Int,
        indeterminateBoolean: Boolean
    ) {

        val deathNotify = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(getColor(R.color.colorRed))
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setProgress(progressCurrent, progressEnd, indeterminateBoolean)
            .setPriority(priorityType)
            .setAutoCancel(false)
        // notify

        with(NotificationManagerCompat.from(this)) {
            notify(notify_ID, deathNotify.build())
            notify_ID += 1
        }
    }
}
