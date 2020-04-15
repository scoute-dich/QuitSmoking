package de.baumann.quitsmoking.about

import android.content.Context
import com.danielstone.materialaboutlibrary.MaterialAboutFragment
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import de.baumann.quitsmoking.about.AboutContent.createMaterialAboutList

class AboutFragment : MaterialAboutFragment() {
    override fun getMaterialAboutList(c: Context): MaterialAboutList {
        return createMaterialAboutList(c)
    }
}