package com.koli.openvocab.view.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.model.Question;
import com.koli.openvocab.model.StreakCounter;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryStatsDao;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.dao.WordStatsDao;
import com.koli.openvocab.service.QuestionProvider;
import com.koli.openvocab.settings.QuizSettings;
import com.koli.openvocab.settings.SettingsProvider;
import com.koli.openvocab.view.adapter.QuizAdapter;

import java.util.UUID;
import java.util.stream.IntStream;

public class QuizActivity extends AppCompatActivity {

    private TextView questionView;
    private RecyclerView answerView;

    private MenuItem streakItem;

    private SettingsProvider settings;
    private StreakCounter streakCounter;

    private QuestionProvider questionProvider;
    private Question question;

    private DictionaryStatsDao dictionaryStatsDao;
    private WordStatsDao wordStatsDao;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        UUID dictionaryId = UUID.fromString(getIntent().getStringExtra("DICTIONARY"));
        WordDao wordDao = AppDatabase.getInstance(this).wordDao();
        this.questionProvider = new QuestionProvider(this, wordDao.findAllInDictionary(dictionaryId));
        this.streakCounter = new StreakCounter(dictionaryId);

        questionView = findViewById(R.id.question_view);
        answerView = findViewById(R.id.answer_grid);

        this.settings = new SettingsProvider(this);
        this.wordStatsDao = AppDatabase.getInstance(this).scoreDao();
        this.dictionaryStatsDao = AppDatabase.getInstance(this).dictionaryStatsDao();

        findViewById(R.id.constraintLayout).setOnClickListener((v) -> skipToNextQuestionWhenAnswered());

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
        if (question.isAnswered()) {
            skipToNextQuestionWhenAnswered();
            return;
        }

        question.answer(view.getWord());
        if (question.isCorrect()) {
            wordStatsDao.addCorrect(view.getWord().getId());
            view.getChoiceButton().setBackgroundColor(Color.GREEN);
            streakCounter.increment();
        } else {
            wordStatsDao.addIncorrect(view.getWord().getId());
            view.getChoiceButton().setBackgroundColor(Color.RED);
            findAndHighlightCorrectAnswer();
            dictionaryStatsDao.updateStreakWhenHigher(streakCounter.getDictionaryId(), streakCounter.getCurrentStreak());
            streakCounter.reset();
        }
        streakItem.setTitle(streakCounter.asString());
        waitThenGetQuestion();
    }

    private void findAndHighlightCorrectAnswer() {
        IntStream.range(0, answerView.getChildCount()).forEach(i -> {
            if (answerView.findViewHolderForAdapterPosition(i) instanceof QuizAdapter.ViewHolder viewHolder) {
                if (viewHolder.getWord().equals(question.getWord())) {
                    viewHolder.getChoiceButton().setBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    private void skipToNextQuestionWhenAnswered() {
        if(question.isAnswered()) {
            handler.removeCallbacksAndMessages(null);
            nextQuestion();
        }
    }

    private void waitThenGetQuestion() {
        handler.postDelayed(this::nextQuestion, settings.readInt(QuizSettings.WAIT_TIME));
    }

}
