package de.baumann.quitsmoking.about

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import de.baumann.quitsmoking.R
import de.baumann.quitsmoking.helper.ActivityIntro

internal object AboutContent {
    @JvmStatic
    fun createMaterialAboutList(c: Context): MaterialAboutList {
        val appCardBuilder = MaterialAboutCard.Builder()

        // Add items to card
        appCardBuilder.addItem(MaterialAboutTitleItem.Builder()
                .text(R.string.app_name)
                .icon(R.mipmap.ic_launcher)
                .build())
        try {
            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    ContextCompat.getDrawable(c, R.drawable.earth2),
                    c.getString(R.string.about_version),
                    false))
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        appCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_changelog)
                .subText(R.string.about_changelog_summary)
                .icon(R.drawable.format_list_bulleted)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/scoute-dich/QuitSmoking/blob/master/CHANGELOG.md")))
                .build())
        appCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_license)
                .subText(R.string.about_license_summary)
                .icon(R.drawable.copyright)
                .setOnClickAction {
                    val s: SpannableString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        SpannableString(Html.fromHtml(c.getString(R.string.about_text), Html.FROM_HTML_MODE_LEGACY))
                    } else {
                        SpannableString(Html.fromHtml(c.getString(R.string.about_text)))
                    }
                    Linkify.addLinks(s, Linkify.WEB_URLS)
                    val d = AlertDialog.Builder(c)
                            .setTitle(R.string.about_title)
                            .setMessage(s)
                            .setPositiveButton(c.getString(R.string.yes)
                            ) { dialog, id -> dialog.cancel() }.show()
                    d.show()
                    (d.findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
                }
                .build())
        appCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_intro)
                .subText(R.string.about_intro_summary)
                .icon(R.drawable.information_outline_dark)
                .setOnClickAction {
                    val intent = Intent(c, ActivityIntro::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    c.startActivity(intent)
                }
                .build())
        val authorCardBuilder = MaterialAboutCard.Builder()
        authorCardBuilder.title(R.string.about_title_dev)
        authorCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_dev)
                .subText(R.string.about_dev_summary)
                .icon(R.drawable.gaukler_faun)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/scoute-dich/")))
                .build())
        authorCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_donate)
                .subText(R.string.about_donate_summary)
                .icon(R.drawable.coin)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=NP6TGYDYP9SHY")))
                .build())
        val authorCardBuilder2 = MaterialAboutCard.Builder()
        authorCardBuilder2.title(R.string.about_title_ext)
        authorCardBuilder2.addItem(MaterialAboutActionItem.Builder()
                .text("Neofaum")
                .subText(R.string.about_title_ext3)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/naofum")))
                .build())
        authorCardBuilder2.addItem(MaterialAboutActionItem.Builder()
                .text("Primokorn")
                .subText(R.string.about_title_ext4)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/Primokorn")))
                .build())
        authorCardBuilder2.addItem(MaterialAboutActionItem.Builder()
                .text("Mesnevi")
                .subText(R.string.about_title_ext5)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/mesnevi")))
                .build())
        authorCardBuilder2.addItem(MaterialAboutActionItem.Builder()
                .text("igi0")
                .subText(R.string.about_title_ext6)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://gitlab.com/igi0")))
                .build())
        val convenienceCardBuilder = MaterialAboutCard.Builder()
        convenienceCardBuilder.title(R.string.about_title_libs)
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Android Onboarder")
                .subText(R.string.about_license_3)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Android Onboarder", "https://github.com/chyrta/AndroidOnboarder", true, false))
                .build())
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Glide")
                .subText(R.string.about_license_9)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Glide", "https://github.com/bumptech/glide", true, false))
                .build())
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Material About Library")
                .subText(R.string.about_license_7)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Material About Library", "https://github.com/daniel-stoneuk/material-about-library", true, false))
                .build())
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Material Date Time Picker")
                .subText(R.string.about_license_2)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Material Date Time Picker", "https://github.com/wdullaer/MaterialDateTimePicker", true, false))
                .build())
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Material Design Icons")
                .subText(R.string.about_license_8)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Material Design Icons", "https://github.com/Templarian/MaterialDesign", true, false))
                .build())
        convenienceCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("RxImagePicker")
                .subText(R.string.about_license_1)
                .icon(R.drawable.github_circle)
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "RxImagePicker", "https://github.com/MLSDev/RxImagePicker", true, false))
                .build())
        return MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), authorCardBuilder2.build(), convenienceCardBuilder.build())
    }
}