package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questiontextview;
    private TextView questioncountertextview;
    private Button truebutton;
    private Button falsebutton;
    private ImageButton prevbutton;
    private ImageButton nextbutton;
    private int currentquestionindex = 0;
    private List<Question> questionList;
    private TextView highscore;

    private int scorecount = 0;
    private Score score;
    private TextView scoretext;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(MainActivity.this);
        scoretext = findViewById(R.id.score_text);

        highscore = findViewById(R.id.highest_score);
        nextbutton = findViewById(R.id.next_button);
        prevbutton = findViewById(R.id.previous_button);
        truebutton = findViewById(R.id.true_button);
        falsebutton = findViewById(R.id.false_button);
        questioncountertextview = findViewById(R.id.counter_text);
        questiontextview = findViewById(R.id.question_textview);

        nextbutton.setOnClickListener(this);
        prevbutton.setOnClickListener(this);
        truebutton.setOnClickListener(this);
        falsebutton.setOnClickListener(this);

        highscore.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(prefs.gethighscore())));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questiontextview.setText(questionArrayList.get(currentquestionindex).getAnswer());
                questioncountertextview.setText(currentquestionindex + " / " + questionArrayList.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.previous_button:
                if(currentquestionindex > 0){
                    currentquestionindex = (currentquestionindex - 1) % questionList.size();
                    updatequestion();
                }
                break;
            case R.id.next_button:
                currentquestionindex = (currentquestionindex + 1) % questionList.size();
                updatequestion();
                break;
            case R.id.true_button:
                checkans(true);
                updatequestion();
                break;
            case R.id.false_button:
                checkans(false);
                updatequestion();
                break;
        }
    }

    private void checkans(boolean b) {
        boolean answeristrue = questionList.get(currentquestionindex).getAnswertrue();
        int toastmessageid = 0;
        if(b == answeristrue){
            shakeAnimation();
            fadeView();
            addpoints();
            toastmessageid = R.string.correct_answer;
        }else{
            deductpoints();
            shakeAnimation();
            toastmessageid = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastmessageid,Toast.LENGTH_SHORT).show();
    }

    private void addpoints() {
        scorecount += 100;
        score.setScore(scorecount);
        scoretext.setText(String.valueOf(score.getScore()));
    }
    private void deductpoints() {
        scorecount -= 100;
        if(scorecount > 0) {
            score.setScore(scorecount);
            scoretext.setText(String.valueOf(score.getScore()));
        }else{
            scorecount = 0;
            score.setScore(scorecount);
            scoretext.setText(String.valueOf(score.getScore()));
        }
        score.setScore(scorecount);
    }

    private void updatequestion() {
        String question = questionList.get(currentquestionindex).getAnswer();
        questiontextview.setText(question);
        questioncountertextview.setText(currentquestionindex + " / " + questionList.size());
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView2);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentquestionindex = (currentquestionindex + 1) % questionList.size();
                updatequestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView2);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentquestionindex = (currentquestionindex + 1) % questionList.size();
                updatequestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {

        prefs.savehighscore(score.getScore());
        super.onPause();
    }
}