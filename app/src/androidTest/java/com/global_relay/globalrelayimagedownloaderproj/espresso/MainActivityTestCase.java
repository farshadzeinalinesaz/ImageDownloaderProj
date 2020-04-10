package com.global_relay.globalrelayimagedownloaderproj.espresso;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.view.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class MainActivityTestCase
{
    @Rule
    public androidx.test.rule.ActivityTestRule<MainActivity> mainActivityTestRule= new androidx.test.rule.ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void viewPagerTestCase()
    {
        Espresso.onView(ViewMatchers.withId(R.id.viewPagerContent))
                .perform(ViewActions.swipeLeft())
                .perform(ViewActions.swipeLeft());
        Espresso.onView(ViewMatchers.withId(R.id.btnNextStep))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void backButtonIsDisableTestCase()
    {
        Espresso.onView(ViewMatchers.withId(R.id.viewPagerContent))
                .perform(ViewActions.swipeLeft())
                .perform(ViewActions.swipeRight());

        Espresso.onView(ViewMatchers.withId(R.id.btnBackStep))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


    /*@Test
    public void registerNewPropertyTestCase()
    {
        Espresso.onView(ViewMatchers.withId(R.id.floatingButton))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.editTitle))
                .perform(ViewActions.typeText("Property Title"))
                .check(ViewAssertions.matches(ViewMatchers.withText("Property Title")));

        Espresso.onView(ViewMatchers.withId(R.id.editRent))
                .perform(ViewActions.scrollTo(),ViewActions.typeText("100000"));

        Espresso.onView(ViewMatchers.withId(R.id.editSize))
                .perform(ViewActions.scrollTo(),ViewActions.typeText("500"));

        Espresso.onView(ViewMatchers.withId(R.id.editDuration))
                .perform(ViewActions.scrollTo(),ViewActions.typeText("12"));

        Espresso.onView(ViewMatchers.withId(R.id.editBedroomCount))
                .perform(ViewActions.scrollTo(),ViewActions.typeText("3"));

        Espresso.onView(ViewMatchers.withId(R.id.editBathroomCount))
                .perform(ViewActions.scrollTo(),ViewActions.typeText("2"));

        Espresso.onView(ViewMatchers.withId(R.id.hydroId))
                .perform(ViewActions.scrollTo(),ViewActions.click());
    }*/
}
