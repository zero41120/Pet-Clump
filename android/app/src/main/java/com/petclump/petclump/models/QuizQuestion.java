package com.petclump.petclump.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.petclump.petclump.R;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestion {
    private static final String TAG = "Quiz Question";
    public static final int NO = 0, YES = 1, SKIP = 2;
    public static List<String> defaultQuestions = null;
    public static List<String> getQuestion(Context c, String quizString, Integer count){
        if (defaultQuestions == null) {
            defaultQuestions = new ArrayList<String>() {{
                add(c.getString(R.string.Do_you_like_to_answer_questions));
                add(c.getString(R.string.Do_you_spend_most_of_your_money_for_your_pet));
                add(c.getString(R.string.Do_you_think_your_pet_gradually_looks_similar_to_you));
                add(c.getString(R.string.Do_you_beat_your_pet));
                add(c.getString(R.string.Have_your_pet_ever_sleepwalked));
                add(c.getString(R.string.Do_you_think_that_your_pet_is_cute));
                add(c.getString(R.string.Does_your_pet_love_flowers));
                add(c.getString(R.string.Do_you_talk_to_your_pet_everyday));
                add(c.getString(R.string.Does_your_pet_love_the_beach));
                add(c.getString(R.string.Does_your_pet_love_to_listen_to_music));
                add(c.getString(R.string.Does_your_pet_make_you_angry_sometimes));
                add(c.getString(R.string.Does_your_pet_smoke_weeds));
                add(c.getString(R.string.Does_your_pet_run_faster_than_you));
                add(c.getString(R.string.Is_your_pet_nearsighted));
                add(c.getString(R.string.Does_your_pet_love_to_take_a_bath));
                add(c.getString(R.string.Can_your_pet_be_put_into_a_jar));
                add(c.getString(R.string.Does_your_pet_love_surfing));
                add(c.getString(R.string.Do_you_think_your_pet_regard_you_as_a_servant));
                add(c.getString(R.string.Does_your_pet_get_along_pretty_well_with_other_pets));
                add(c.getString(R.string.Do_you_take_your_pet_outside));
                add(c.getString(R.string.Do_you_sing_to_your_pet));
                add(c.getString(R.string.Is_your_pet_well_mannered));
                add(c.getString(R.string.Do_you_like_short_hair_animal));
                add(c.getString(R.string.Do_you_always_feed_your_pet_on_time));
                add(c.getString(R.string.Does_your_pet_has_a_dream));
                add(c.getString(R.string.Does_your_pet_ever_want_to_eat_you));
                add(c.getString(R.string.Can_you_bear_that_when_your_pet_bothers_you));
                add(c.getString(R.string.Do_you_tickle_your_pet));
                add(c.getString(R.string.If_your_pet_turns_into_human_one_day__would_you_want_to_date_them));
                add(c.getString(R.string.Does_your_pet_love_to_go_to_a_park));
                add(c.getString(R.string.Does_your_pet_bark_meow));
                add(c.getString(R.string.Can_your_salary_easily_afford_your_pet));
                add(c.getString(R.string.Do_you_love_your_pet));
                add(c.getString(R.string.Does_your_pet_attack_other_pets));
                add(c.getString(R.string.Does_your_pet_love_swimming));
                add(c.getString(R.string.Does_your_pet_recognize_its_name_when_you_call_it));
                add(c.getString(R.string.Do_you_take_your_pet_to_your_office_or_school));
                add(c.getString(R.string.Does_your_pet_love_sunshine));
                add(c.getString(R.string.Do_you_think_sterilization_is_fair_for_pets));
                add(c.getString(R.string.Does_your_pet_randomly_poo));
                add(c.getString(R.string.Is_your_pet_pregnant));
                add(c.getString(R.string.Do_you_like_long_hair_animal));
                add(c.getString(R.string.Is_your_pet_introvert_or_extrovert));
                add(c.getString(R.string.Do_you_say_goodnight_to_your_pet_before_you_go_to_sleep));
                add(c.getString(R.string.Have_your_pet_mated_before));
                add(c.getString(R.string.Do_you_kiss_your_pet));
                add(c.getString(R.string.Do_you_feel_energized_when_your_pet_is_around_you));
                add(c.getString(R.string.Does_your_pet_wear_costumes));
                add(c.getString(R.string.Does_your_pet_love_can_food));
                add(c.getString(R.string.Do_you_sleep_with_your_pet_next_to_you));
                add(c.getString(R.string.Have_you_ever_wished_that_you_and_your_pet_can_switch_identity));
                add(c.getString(R.string.Do_you_walk_with_your_pet_on_leash));
                add(c.getString(R.string.Does_your_pet_love_watching_TV));
                add(c.getString(R.string.Do_you_cuddle_with_your_pet));
                add(c.getString(R.string.Do_you_pet_different_species_of_animals));
                add(c.getString(R.string.Will_you_be_distracted_by_your_pet_because_your_pets_are_too_lovely));
                add(c.getString(R.string.Does_your_pet_love_money));
                add(c.getString(R.string.Is_your_pet_afraid_of_you));
                add(c.getString(R.string.Have_your_pet_done_a_surgery_before));
                add(c.getString(R.string.Is_your_pet_smart));
                add(c.getString(R.string.Do_you_want_to_buy_more_pets));
                add(c.getString(R.string.Do_you_prefer_big_pets_or_small_pets));
                add(c.getString(R.string.Do_you_train_your_pet_to_do_interesting_activities));
                add(c.getString(R.string.Are_you_a_vegan));
                add(c.getString(R.string.Does_your_pet_chase_other_people_around));
                add(c.getString(R.string.Does_your_pet_love_jungle));
                add(c.getString(R.string.Do_you_think_about_your_pet_when_they_are_not_with_you));
                add(c.getString(R.string.Is_your_pet_long_hair));
                add(c.getString(R.string.Do_you_buy_high_quality_food_to_your_pet));
                add(c.getString(R.string.Can_you_bear_your_pet_to_date_with_other_pets_and_make_you_lonely));
            }};
        }

        int begin = quizString.length();
        int end = begin + count;
        if (end < defaultQuestions.size() +1){
            return defaultQuestions.subList(begin, end);
        }
        Log.d(TAG, "getQuestion: answered all questions!" + end + " : " + defaultQuestions.size());
        return null;
    }
    public static Integer getNumberOfAvaliableQuestions(Context c){
        if (defaultQuestions != null){
            return defaultQuestions.size();
        }
        getQuestion(c, "", 10);
        return defaultQuestions.size();
    }
}
