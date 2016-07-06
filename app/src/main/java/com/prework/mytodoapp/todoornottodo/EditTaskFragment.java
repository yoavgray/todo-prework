package com.prework.mytodoapp.todoornottodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditTaskFragment extends DialogFragment {
    private static final String ARG_TEXT = "text";
    private static final String ARG_POSITION = "position";

    private EditText etEditText;
    private String mText;
    private int mPosition;

    //instantiate a new dialog fragment and save arguments
    public static EditTaskFragment newInstance(String text, int position) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(ARG_TEXT);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_task, container, false);
        setupViews(v);
        getDialog().setTitle(R.string.edit_task);
        return v;
    }

    private void setupViews(View rootView) {
        etEditText = (EditText)rootView.findViewById(R.id.etEditTask);
        etEditText.setText(mText);
        etEditText.setSelection(mText.length());
        Button ok = (Button) rootView.findViewById(R.id.buttonEditTaskOk);
        Button cancel = (Button) rootView.findViewById(R.id.buttonEditTaskCancel);

        //sending the relevant values back to MainActivity
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSelectValue(etEditText.getText().toString(), mPosition);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSelectValue(null, 0);
                dismiss();
            }
        });
    }
}
