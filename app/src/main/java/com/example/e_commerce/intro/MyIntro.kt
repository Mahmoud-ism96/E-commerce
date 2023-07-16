package com.example.e_commerce.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.github.appintro.AppIntro

class MyIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(FirstIntroFragment())
        addSlide(SecondIntroFragment())
        addSlide(ThirdIntroFragment())
        addSlide(FourthIntroFragment())
        setBarColor(resources.getColor(R.color.black))
        setSeparatorColor(resources.getColor(R.color.white))
        setNextArrowColor(resources.getColor(R.color.white))
        setColorDoneText(resources.getColor(R.color.white))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}