package com.alcorp.storyapp.ui.add

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.alcorp.storyapp.R
import com.alcorp.storyapp.helper.EspressoIdlingResource
import com.alcorp.storyapp.ui.main.MainActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddStoryActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(AddStoryActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun takePhotoSuccess() {
        onView(withId(R.id.btnCamera)).perform(click())
        onView(withId(R.id.cameraActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform(click())
//        Espresso.onView(ViewMatchers.withId(R.id.addStoryActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        Intents.intended(IntentMatchers.hasComponent(AddStoryActivity::class.java.name))
//        onView(withId(R.id.uploadFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadPhotoSuccess() {
        onView(withId(R.id.btnCamera)).perform(click())
        onView(withId(R.id.cameraActivity)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform(click())
//        intended(hasComponent(AddStoryActivity::class.java.name))
        onView(withId(R.id.scrollAddStory)).perform(scrollTo())
        onView(withId(R.id.edtDescription)).perform(typeText("Espresso Test"), closeSoftKeyboard())
        onView(withId(R.id.btnUpload)).perform(click())
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()))
//        intended(hasComponent(MainActivity::class.java.name))
//        Espresso.onView(ViewMatchers.withId(R.id.rvStory)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}