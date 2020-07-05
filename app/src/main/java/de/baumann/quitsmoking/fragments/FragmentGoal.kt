package de.baumann.quitsmoking.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import de.baumann.quitsmoking.R
import de.baumann.quitsmoking.helper.HelperMain
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FragmentGoal : Fragment() {
    private lateinit var viewImage: ImageView
    private val rotate: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_goal, container, false)
        PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        viewImage = rootView.findViewById(R.id.imageView)
        if (sharedPreferences.getBoolean(rotate, false)) {
            viewImage.rotation = 0f
        } else {
            viewImage.rotation = 90f
        }
        val path = sharedPreferences.getString("image_goal", "")
        val imgFile = File(path)
        if (path?.isNotEmpty()!! && imgFile.exists()) {
            Glide.with(activity!!)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(viewImage) //imageView to set thumbnail to
        } else {
            viewImage.setImageResource(R.drawable.file_image_dark)
        }
        val goalTitle = sharedPreferences.getString("goalTitle", "")
        val textviewGoaltitle = rootView.findViewById<TextView>(R.id.text_header1)
        if (goalTitle!!.isEmpty()) {
            textviewGoaltitle.text = getString(R.string.not_set1)
        } else {
            textviewGoaltitle.text = goalTitle
        }
        val goaldateNext = sharedPreferences.getLong("goalDate_next", 0)
        val currency = sharedPreferences.getString("currency", "1")
        val cigNumb = sharedPreferences.getString("cig", "")
        val savedMoney = sharedPreferences.getString("costs", "")
        val goalCosts = sharedPreferences.getString("goalCosts", "")
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateStart = format.format(sharedPreferences.getLong("startTime", 0))
        val goalDate = format.format(sharedPreferences.getLong("goalDate_next", 0))
        val dateStop = format.format(date)
        try {
            val d1: Date? = if (goaldateNext == 0L) {
                format.parse(dateStart)
            } else {
                format.parse(goalDate)
            }

            //Time Difference
            val d2 = format.parse(dateStop)
            val diff = d2.time.minus(d1?.time!!)

            //Number of Cigarettes
            val cigNumber = cigNumb!!.toLong()
            val cigDay = 86400000 / cigNumber
            val diffCig = diff / cigDay
            val cigSaved = diffCig.toString()

            //Saved Money
            val costCig = java.lang.Double.valueOf(savedMoney!!.trim { it <= ' ' })
            val sa = cigSaved.toLong().toDouble()
            val cost = sa * costCig
            val costString = String.format(Locale.US, "%.2f", cost)

            //Remaining costs
            val goalCost = goalCosts!!.toLong().toDouble()
            val remCost = goalCost - cost
            val remCostString = String.format(Locale.US, "%.2f", remCost)
            val goalCostString = String.format(Locale.US, "%.2f", goalCost)

            //Remaining time
            val savedMoneyDay = cigNumber * costCig
            val remTime = remCost / savedMoneyDay
            val remTimeString = String.format(Locale.US, "%.1f", remTime)
            val textviewGoalcost: TextView = rootView.findViewById(R.id.text_description1)
            if (goalTitle.isEmpty()) {
                textviewGoalcost.text = getString(R.string.not_set1)
            } else {
                when (currency) {
                    "1" -> textviewGoalcost.text = getString(R.string.costs) + " " + goalCostString + " " + getString(R.string.money_euro) +
                            " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_euro) +
                            " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_euro)
                    "2" -> textviewGoalcost.text = getString(R.string.costs) + " " + goalCostString + " " + getString(R.string.money_dollar) +
                            " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_dollar) +
                            " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_dollar)
                    "3" -> textviewGoalcost.text = getString(R.string.costs) + " " + goalCostString + " " + getString(R.string.money_pound) +
                            " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_pound) +
                            " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_pound)
                    "4" -> textviewGoalcost.text = getString(R.string.costs) + " " + goalCostString + " " + getString(R.string.money_yen) +
                            " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_yen) +
                            " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_yen)
                }
            }
            val textviewGoaltime: TextView = rootView.findViewById(R.id.text_description2)
            if (goalTitle.isEmpty()) {
                textviewGoaltime.text = getString(R.string.not_set1)
            } else {
                if (remTime < 0) {
                    textviewGoaltime.text = getString(R.string.health_congratulations)
                } else {
                    textviewGoaltime.text = (getString(R.string.health_reached) + " "
                            + remTimeString + " " + getString(R.string.time_days))
                }
            }
            val progressBar: ProgressBar? = rootView.findViewById(R.id.progressBar)
            assert(progressBar != null)
            val max = goalCost.toInt()
            val actual = cost.toInt()
            progressBar?.max = max
            progressBar?.progress = actual
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val path = sharedPreferences.getString("image_goal", "")
        val imgFile = File(path)
        if (path!!.isNotEmpty() && imgFile.exists()) {
            Glide.with(activity!!)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(viewImage) //imageView to set thumbnail to
        } else {
            viewImage.setImageResource(R.drawable.file_image_dark)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_backup).isVisible = false
        menu.findItem(R.id.action_share).isVisible = false
        menu.findItem(R.id.action_sort).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
        menu.findItem(R.id.action_reset).isVisible = false
        menu.findItem(R.id.action_info).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_imageLoad) {
            val options = arrayOf<CharSequence>(
                    getString(R.string.choose_gallery),
                    getString(R.string.choose_camera))
            val dialog = AlertDialog.Builder(activity)
            dialog.setPositiveButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            dialog.setItems(options) { _, item ->
                if (options[item] == getString(R.string.choose_gallery)) {
                    RxImagePicker.with(activity).requestImage(Sources.GALLERY)
                            .flatMap { uri -> RxImageConverters.uriToFile(activity, uri, File(context?.getExternalFilesDir(null).toString() + "/Android/data/de.baumann.quitsmoking/" + HelperMain.newFileName())) }.subscribe { file -> // Do something with your file copy
                                sharedPreferences.edit().putString("image_goal", file.absolutePath).apply()
                                Glide.with(activity!!)
                                        .load(file)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .fitCenter()
                                        .into(viewImage)
                            }
                }
                if (options[item] == getString(R.string.choose_camera)) {
                    RxImagePicker.with(activity).requestImage(Sources.CAMERA)
                            .flatMap { uri -> RxImageConverters.uriToFile(activity, uri, File(context?.getExternalFilesDir(null).toString() + "/Android/data/de.baumann.quitsmoking/" + HelperMain.newFileName())) }.subscribe { file -> // Do something with your file copy
                                sharedPreferences.edit().putString("image_goal", file.absolutePath).apply()
                                Glide.with(activity!!)
                                        .load(file)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .fitCenter()
                                        .into(viewImage)
                                val f = lastFileModified(context?.getExternalFilesDir(null).toString() + File.separator + "Pictures")
                                f!!.delete()
                            }
                }
            }
            dialog.show()
        }
        if (id == R.id.action_imageRotate) {
            if (sharedPreferences.getBoolean(rotate, true)) {
                viewImage.rotation = 90f
                sharedPreferences.edit().putBoolean(rotate, false).apply()
            } else {
                viewImage.rotation = 0f
                sharedPreferences.edit().putBoolean(rotate, true).apply()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private fun lastFileModified(dir: String): File? {
            val fl = File(dir)
            val files = fl.listFiles { file -> file.isFile }
            var lastMod = Long.MIN_VALUE
            var choice: File? = null
            for (file in files) {
                if (file.lastModified() > lastMod) {
                    choice = file
                    lastMod = file.lastModified()
                }
            }
            return choice
        }
    }
}