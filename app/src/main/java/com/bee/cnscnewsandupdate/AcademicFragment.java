package com.bee.cnscnewsandupdate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class AcademicFragment extends Fragment {

    private Context context;
    private ScholarshipActivity activity;

    public AcademicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic, container, false);
        context = getContext();
        activity = (ScholarshipActivity) getActivity();

        init(view);
        return view;
    }

    private void init(View view) {
        final AppCompatButton button = view.findViewById(R.id.button);
        final AppCompatTextView title = view.findViewById(R.id.title);
        title.setText(activity.getScholarship().getTitle());

        button.setOnClickListener(v -> {
            activity.next();
        });
    }
}