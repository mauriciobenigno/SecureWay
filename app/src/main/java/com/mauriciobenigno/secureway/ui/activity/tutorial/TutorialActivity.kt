package com.mauriciobenigno.secureway.ui.activity.tutorial

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.mauriciobenigno.secureway.R


class TutorialActivity : AppIntro()  {

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onCreate(savedInstanceState, persistentState)

        addSlide(AppIntroFragment.newInstance(
            title = "Welcome...",
            description = "This is the first slide of the example"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "...Let's get started!",
            description = "This is the last slide, I won't annoy you more :)"
        ))

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }

}