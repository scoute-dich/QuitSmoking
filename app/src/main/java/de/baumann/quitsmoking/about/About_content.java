package de.baumann.quitsmoking.about;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import de.baumann.quitsmoking.R;
import de.baumann.quitsmoking.helper.Activity_intro;


class About_content {

    static MaterialAboutList createMaterialAboutList(final Context c) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        // Add items to card

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(R.string.app_name)
                .icon(R.mipmap.ic_launcher)
                .build());

        try {

            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    ContextCompat.getDrawable(c, R.drawable.earth2),
                    "Version",
                    false));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.about_changelog)
                .subText(R.string.about_changelog_summary)
                .icon(R.drawable.format_list_bulleted)
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/scoute-dich/QuitSmoking/blob/master/CHANGELOG.md")))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.about_license)
                .subText(R.string.about_license_summary)
                .icon(R.drawable.copyright)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        SpannableString s;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            s = new SpannableString(Html.fromHtml(c.getString(R.string.about_text),Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            //noinspection deprecation
                            s = new SpannableString(Html.fromHtml(c.getString(R.string.about_text)));
                        }

                        Linkify.addLinks(s, Linkify.WEB_URLS);

                        final AlertDialog d = new AlertDialog.Builder(c)
                                .setTitle(R.string.about_title)
                                .setMessage(s)
                                .setPositiveButton(c.getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).show();
                        d.show();
                        ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    }
                })
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.about_intro)
                .subText(R.string.about_intro_summary)
                .icon(R.drawable.information_outline_dark)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(c, Activity_intro.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        c.startActivity(intent);
                    }
                })
                .build());


        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title(R.string.about_title_dev);

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.about_dev)
                .subText(R.string.about_dev_summary)
                .icon(R.drawable.gaukler_faun)
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/scoute-dich/")))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.about_donate)
                .subText(R.string.about_donate_summary)
                .icon(R.drawable.coin)
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=NP6TGYDYP9SHY")))
                .build());

        MaterialAboutCard.Builder authorCardBuilder2 = new MaterialAboutCard.Builder();
        authorCardBuilder2.title(R.string.about_title_ext);

        authorCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .text("Neofaum")
                .subText(R.string.about_title_ext3)
                .icon(R.drawable.github_circle)
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/scoute-dich/QuitSmoking/releases")))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();
        convenienceCardBuilder.title(R.string.about_title_libs);

        convenienceCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Android Onboarder")
                .subText(R.string.about_license_3)
                .icon(R.drawable.github_circle)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Android Onboarder", "https://github.com/chyrta/AndroidOnboarder", true, false))
                .build());

        convenienceCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Material About Library")
                .subText(R.string.about_license_7)
                .icon(R.drawable.github_circle)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Material About Library", "https://github.com/daniel-stoneuk/material-about-library", true, false))
                .build());

        convenienceCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Material Design Icons")
                .subText(R.string.about_license_8)
                .icon(R.drawable.github_circle)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Material Design Icons", "https://github.com/Templarian/MaterialDesign", true, false))
                .build());
        
        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), authorCardBuilder2.build(), convenienceCardBuilder.build());
    }

}
