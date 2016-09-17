package com.prework.mytodoapp.todoornottodo.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.prework.mytodoapp.todoornottodo.activities.MainActivity;
import com.prework.mytodoapp.todoornottodo.R;

public class LikeDislikeFragment extends DialogFragment {
    private static final int USER_TEXT_STARTING_INDEX = 20;

    private EditText etEditMail;

    //instantiate a new dialog fragment and save arguments
    public static LikeDislikeFragment newInstance() {
        return new LikeDislikeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_like_dislike, container, false);
        setupViews(v);
        getDialog().setTitle(R.string.send_mail);
        return v;
    }

    private void setupViews(View rootView) {
        etEditMail = (EditText)rootView.findViewById(R.id.et_edit_mail);
        Button sendMail = (Button) rootView.findViewById(R.id.bt_send_mail);
        Button cancel = (Button) rootView.findViewById(R.id.bt_cancel_send_mail);

        //sending the relevant values back to MainActivity
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etEditMail.getText().toString();

                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSendMail(userEmail);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}