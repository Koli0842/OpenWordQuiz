package com.koli.openquiz.view.statistics;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koli.openquiz.R;
import com.koli.openquiz.model.Score;

import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private Context context;
    private Score score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View _view, Bundle savedInstanceState) {
        TextView highestStreak = (TextView) getView().findViewById(R.id.highestStreak);
        TextView accuracy = (TextView) getView().findViewById(R.id.accuracy);

        highestStreak.setText(String.format(Locale.getDefault(),"%d", score.getHighestStreak()));
        accuracy.setText(String.format(Locale.getDefault(), "%.1f%%", score.getAccuracy()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        this.score = new Score(context);
    }
}
