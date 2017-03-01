package com.example.captainmccann.talkanalyzer.talks;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;


import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.TypeOfTalk;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.intent.Checks.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by CaptainMcCann on 1/31/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TalksAndroidTest {

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Rule
    public ActivityTestRule<TalksActvity> talksActivityTestRule =
            new ActivityTestRule<>(TalksActvity.class);

    @Test
    public void navViewOpens_onHomeIconPressed() {
        onView(withId(R.id.talks_drawer_layout)).check(matches(isClosed(Gravity.START)));

        String navigateUpDesc = talksActivityTestRule.getActivity().getString(
                android.support.v7.appcompat.R.string.abc_action_bar_up_description
        );
        onView(withContentDescription(navigateUpDesc)).perform(click());

        onView(withId(R.id.talks_drawer_layout)).check(matches(isOpen(Gravity.START)));
    }

    @Test
    public void checkStatsNavItem_opensStatsActivity() {
        onView(withId(R.id.talks_drawer_layout)).check(matches(isClosed(Gravity.START)))
                .perform(open());

        onView(withId(R.id.talks_nav_view)).perform(navigateTo(R.id.stats));

        onView(withId(R.id.textView14)).check(matches(withText(R.string.total_talks)));
    }

    @Test
    public void clickFAB_opensNewTalkUI() {
        onView(withId(R.id.talks_fab)).perform(click());

        onView(withId(R.id.new_talk_title)).check(matches(isDisplayed()));
    }

    @Test
    public void addTalkToTalksList() {
        String newTalkTitle = "EspressoTest";
        String newTalkSpeaker = "Somebody";
        String spinnerSelection = TypeOfTalk.PUBLIC.getType();

        onView(withId(R.id.talks_fab)).perform(click());

        onView(withId(R.id.new_talk_title)).perform(typeText(newTalkTitle));
        onView(withId(R.id.new_talk_speaker)).perform(typeText(newTalkSpeaker));
        onView(withId(R.id.new_talk_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(spinnerSelection))).perform(click());

        onView(withId(R.id.new_talk_confirm)).perform(click());

        onView(withId(R.id.new_talk_confirm)).perform(click());
    }

    @Test
    public void clickTalk_opensTalkDetails() {
        String newTalkTitle = "EspressoTest";
        onView(withItemText(newTalkTitle)).check(matches(isDisplayed()));

        onView(withItemText(newTalkTitle)).perform(click());

        onView(withId(R.id.talk_detail_title)).check(matches(isDisplayed()));
    }

    @Test
    public void saveTalk_refreshesTalkList() {
        String newTalkTitle = "EspressoTest";
        addTalkToTalksList();

        onView(withItemText(newTalkTitle)).check(matches(isDisplayed()));
    }
}
