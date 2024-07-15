package com.bee.cnscnewsandupdate;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class CustomTextWatcher implements TextWatcher {

    TextInputLayout textInputEditText;

    public CustomTextWatcher(TextInputLayout textInputEditText) {
        this.textInputEditText = textInputEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (textInputEditText.getError() != null) {
            textInputEditText.setError(null);
        }
    }
}
