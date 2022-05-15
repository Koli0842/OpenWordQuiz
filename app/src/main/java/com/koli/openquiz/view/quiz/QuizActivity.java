package com.koli.openquiz.view.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.koli.openquiz.R;
import com.koli.openquiz.model.Question;
import com.koli.openquiz.model.Score;
import com.koli.openquiz.service.QuestionProvider;
import com.koli.openquiz.settings.QuizSettings;
import com.koli.openquiz.settings.SettingsProvider;
import com.koli.openquiz.view.adapter.ButtonListAdapter;

public class QuizActivity extends AppCompatActivity {

    private TextView questionView;
    private GridView answerView;

    private MenuItem streakItem;

    private SettingsProvider settings;
    private Score score;

    private QuestionProvider questionProvider;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        String filename = getIntent().getStringExtra("DICTIONARY");
        this.questionProvider = new QuestionProvider(this, filename);

        questionView = findViewById(R.id.question_view);
        answerView = findViewById(R.id.answer_grid);

        this.score = new Score(this);
        this.settings = new SettingsProvider(this);

        answerView.setOnItemClickListener(this::onAnswerClick);
        nextQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz, menu);
        streakItem = menu.findItem(R.id.streak);
        return true;
    }

    private void nextQuestion() {
        question = questionProvider.next();
        ButtonListAdapter buttonListAdapter = new ButtonListAdapter(this, question.getChoices());

        questionView.setText(question.getWord().getQuery());
        answerView.setAdapter(buttonListAdapter);
    }

    private void onAnswerClick(AdapterView<?> parent, View view, int position, long id) {
        if (question.isAnswered())
            return;

        if (view instanceof Button button) {
            question.answer(position);
            if (question.isCorrect()) {
                score.addCorrect();
                button.setBackgroundColor(Color.GREEN);
            } else {
                score.addIncorrect();
                button.setBackgroundColor(Color.RED);
                for (int i = 0; i < answerView.getChildCount(); i++) {
                    Button b = (Button) answerView.getChildAt(i);
                    if (b.getTag().equals(question.getWord())) {
                        b.setBackgroundColor(Color.GREEN);
                    }
                }
            }
            updateStreakView();
            waitThenGetQuestion();
        }
    }

    private void updateStreakView() {
        int streak = score.getCurrentStreak();
        streakItem.setTitle(Integer.toString(streak));
    }

    private void waitThenGetQuestion() {
        new Handler().postDelayed(this::nextQuestion, settings.readInt(QuizSettings.WAIT_TIME));
    }

}
