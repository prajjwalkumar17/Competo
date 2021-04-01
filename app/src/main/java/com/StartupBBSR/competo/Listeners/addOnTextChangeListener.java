package com.StartupBBSR.competo.Listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class addOnTextChangeListener implements TextWatcher {
    Context context;
    EditText et;
    TextInputLayout til;

    public addOnTextChangeListener(Context context, TextInputEditText ET, TextInputLayout TIL) {
        super();
        this.context = context;
        this.et = ET;
        this.til = TIL;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        Should remove the error on text change
        til.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
