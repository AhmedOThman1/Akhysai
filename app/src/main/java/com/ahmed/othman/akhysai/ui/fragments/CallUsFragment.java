package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class CallUsFragment extends Fragment {

    public CallUsFragment() {
        // Required empty public constructor
    }

    TextInputLayout name, email, message_title, message_body;

    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_us, container, false);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.contact_us);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        message_title = view.findViewById(R.id.message_title);
        message_body = view.findViewById(R.id.message_body);

        view.findViewById(R.id.send_message).setOnClickListener(v -> {
            if (name.getEditText().getText().toString().trim().isEmpty()) {
                email.setError(null);
                name.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (name.getEditText().getText().toString().trim().length() < 6) {
                email.setError(null);
                name.setError(requireActivity().getResources().getString(R.string.can_not_be_less_than_6));
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (email.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString().trim()).matches()) {
                name.setError(null);
                email.setError(requireActivity().getResources().getString(R.string.enter_valid_email));
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (message_title.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                message_title.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                message_title.requestFocus();
                open_keyboard(message_title.getEditText());
            } else if (message_body.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                message_title.setError(null);
                message_body.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                message_body.requestFocus();
                open_keyboard(message_body.getEditText());
            } else {
                sendMessage(
                        currentUser==null?null:currentUser.getUid(),
                        name.getEditText().getText().toString().trim(),
                        email.getEditText().getText().toString().trim(),
                        message_title.getEditText().getText().toString().trim(),
                        message_body.getEditText().getText().toString().trim());

                Navigation.findNavController(v).popBackStack();
//                NavOptions navOptions = new NavOptions.Builder()
//                        .setPopUpTo(R.id.homeFragment, true)
//                        .setEnterAnim(R.anim.slide_out_left)
//                        .setExitAnim(R.anim.slide_in_right)
//                        .setPopEnterAnim(R.anim.slide_out_right)
//                        .setPopExitAnim(R.anim.slide_in_left)
//                        .build();
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            }
        });

        return view;
    }

    private void sendMessage(String uid, String name, String email, String message_title, String message_body) {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
    }


    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
    }



}