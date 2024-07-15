package com.bee.cnscnewsandupdate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class RequirementsFragment extends Fragment {

    private Context context;
    private ScholarshipActivity activity;
    private ImageView birth_certificate_image, cog_cor_image, valid_id_image;

    public RequirementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requirements, container, false);
        context = getContext();
        activity = (ScholarshipActivity) getActivity();

        init(view);
        return view;
    }

    private void init(View view) {
        final AppCompatTextView title = view.findViewById(R.id.title);
        title.setText(activity.getScholarship().getTitle());

        birth_certificate_image = view.findViewById(R.id.birth_certificate_image);
        cog_cor_image = view.findViewById(R.id.cog_cor_image);
        valid_id_image = view.findViewById(R.id.valid_id_image);
        final CardView birth_certificate = view.findViewById(R.id.birth_certificate);
        final CardView valid_id = view.findViewById(R.id.valid_id);
        final CardView cog_cor = view.findViewById(R.id.cog_cor);
        birth_certificate.setOnClickListener(v -> {
            activity.scan(0);
        });
        valid_id.setOnClickListener(v -> {
            activity.scan(1);
        });
        cog_cor.setOnClickListener(v -> {
            activity.scan(2);
        });

        final AppCompatButton button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            final String bir = activity.getUris().get(0);
            final String val = activity.getUris().get(1);
            final String cog = activity.getUris().get(2);

            if (activity.isAlreadyApplied()) {
                Toast.makeText(context, "Your application is on process.", Toast.LENGTH_SHORT).show();
            } else if (bir.equals("")) {
                Toast.makeText(context, "Add birth certificate", Toast.LENGTH_SHORT).show();
            } else if (val.equals("")) {
                Toast.makeText(context, "Add valid ID", Toast.LENGTH_SHORT).show();
            } else if (cog.equals("")) {
                Toast.makeText(context, "Add cog/cor", Toast.LENGTH_SHORT).show();
            } else {
                activity.apply();
            }
        });
    }

    public void updateViews(int num) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.baseline_check_circle_24);
        switch (num) {
            case 0:
                birth_certificate_image.setImageDrawable(drawable);
                break;
            case 1:
                valid_id_image.setImageDrawable(drawable);
                break;
            default:
                cog_cor_image.setImageDrawable(drawable);
        }
    }
}