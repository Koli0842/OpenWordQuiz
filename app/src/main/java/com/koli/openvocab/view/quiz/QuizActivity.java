package com.koli.openvocab.view.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.model.Question;
import com.koli.openvocab.model.StreakCounter;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryStatsDao;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.dao.WordStatsDao;
import com.koli.openvocab.service.QuestionProvider;
import com.koli.openvocab.settings.QuizSettings;
import com.koli.openvocab.settings.QuizTypeSettings;
import com.koli.openvocab.settings.SettingsProvider;
import com.koli.openvocab.view.adapter.QuizAdapter;
import com.koli.openvocab.view.dictionary.DictionaryActivity;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuizActivity extends AppCompatActivity {

    private final Logger log = Logger.getLogger(DictionaryActivity.class.getName());
    private final Handler handler = new Handler();
    private final Random random = new Random();

    private TextToSpeech tts;
    private TextView questionView;
    private RecyclerView answerView;

    private MenuItem streakItem;

    private SettingsProvider settings;
    private StreakCounter streakCounter;

    private QuestionProvider questionProvider;
    private Question question;

    private DictionaryStatsDao dictionaryStatsDao;
    private WordStatsDao wordStatsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        UUID dictionaryId = UUID.fromString(getIntent().getStringExtra("DICTIONARY"));
        WordDao wordDao = AppDatabase.getInstance(this).wordDao();
        this.questionProvider = new QuestionProvider(this, wordDao.findAllInDictionary(dictionaryId).stream().map(Word::fromEntity).collect(Collectors.toList()));
        this.streakCounter = new StreakCounter(dictionaryId);

        questionView = findViewById(R.id.question_view);
        answerView = findViewById(R.id.answer_grid);

        this.settings = new SettingsProvider(this);
        this.wordStatsDao = AppDatabase.getInstance(this).scoreDao();
        this.dictionaryStatsDao = AppDatabase.getInstance(this).dictionaryStatsDao();

        findViewById(R.id.constraintLayout).setOnClickListener((v) -> skipToNextQuestionWhenAnswered());

        this.tts = new TextToSpeech(this, this::initTts);
    }

    private void initTts(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.setLanguage(Locale.UK) == TextToSpeech.SUCCESS) {
                log.info("Initialized TTS");
            } else {
                log.info("Failed to set language");
            }
        } else {
            log.info("Failed to initialize TTS");
        }
        nextQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz_counters, menu);
        streakItem = menu.findItem(R.id.streak);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }

    private void nextQuestion() {
        question = questionProvider.next();
        QuizAdapter quizAdapter = new QuizAdapter(question.getChoices(), this::onAnswerClick);

        boolean textual = settings.readBoolean(QuizTypeSettings.TEXTUAL_ENABLED);
        boolean verbal = settings.readBoolean(QuizTypeSettings.VERBAL_ENABLED);

        if (textual && verbal) {
            questionView.setText(random.nextInt(100) > settings.readInt(QuizTypeSettings.VERBAL_FREQUENCY) ? question.getWord().getQuery() : "\uD83D\uDD0A");
        } else if (textual) {
            questionView.setText(question.getWord().getQuery());
        } else if (verbal) {
            questionView.setText("\uD83D\uDD0A");
        }

        //if ("\uD83D\uDD0A".contentEquals(questionView.getText())) {
        //    tts.setLanguage(question.getWord().getQueryLocale());
        //    tts.speak(question.getWord().getQuery(), TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
        //}

        tts.setLanguage(question.getWord().getQueryLocale());
        tts.speak(question.getWord().getQuery(), TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());

        questionView.setOnClickListener(v -> tts.speak(question.getWord().getQuery(), TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString()));
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
