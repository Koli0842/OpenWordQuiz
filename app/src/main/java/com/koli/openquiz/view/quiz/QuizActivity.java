package com.koli.openquiz.view.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.koli.openquiz.R;
import com.koli.openquiz.model.Question;
import com.koli.openquiz.model.Score;
import com.koli.openquiz.model.Word;
import com.koli.openquiz.service.QuestionProvider;
import com.koli.openquiz.settings.QuizSettings;
import com.koli.openquiz.settings.SettingsProvider;
import com.koli.openquiz.view.adapter.QuizAdapter;

import java.util.stream.Stream;

public class QuizActivity extends AppCompatActivity {

    private TextView questionView;
    private RecyclerView answerView;

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
        QuizAdapter quizAdapter = new QuizAdapter(question.getChoices(), this::onAnswerClick);

        questionView.setText(question.getWord().getQuery());
        answerView.setAdapter(quizAdapter);
    }

    private void onAnswerClick(QuizAdapter.ViewHolder view) {
        if (question.isAnswered())
            return;

        MaterialButton button = view.getChoice();
        if (button.getTag() instanceof Word word) {
            question.answer(word);
            if (question.isCorrect()) {
                score.addCorrect();
                button.setBackgroundColor(Color.GREEN);
            } else {
                score.addIncorrect();
                button.setBackgroundColor(Color.RED);
                findAndHighlightCorrectAnswer();
            }
            updateStreakView();
            waitThenGetQuestion();
        }
    }

    private void findAndHighlightCorrectAnswer() {
        for (int i = 0; i < answerView.getChildCount(); i++) {
            if (answerView.findViewHolderForAdapterPosition(i) instanceof QuizAdapter.ViewHolder viewHolder) {
                MaterialButton button = viewHolder.getChoice();
                if (button.getTag().equals(question.getWord())) {
                    button.setBackgroundColor(Color.GREEN);
                }
            }
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
