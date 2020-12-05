package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class ConfirmDateFragment extends Fragment {

    public ConfirmDateFragment() {
        // Required empty public constructor
    }

    TextInputLayout phone, address, notes;
    TextView are_you_sure_question_text;
    FirebaseUser currentUser;
    Akhysai currentAkhysai = new Akhysai();
    AvailableDate date = new AvailableDate();
    long start_time, end_time;
    Calendar start = Calendar.getInstance(), end = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_date, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar.setVisibility(View.VISIBLE);
        phone = view.findViewById(R.id.phone);
        address = view.findViewById(R.id.address);
        notes = view.findViewById(R.id.notes);
        are_you_sure_question_text = view.findViewById(R.id.are_you_sure_question_text);

        Bundle args = getArguments();
        if (args == null)
            requireActivity().onBackPressed();
        else {
            String json = args.getString("akhyisa", "");
            if (!json.trim().isEmpty())
                currentAkhysai = new Gson().fromJson(json, Akhysai.class);

            json = args.getString("date", "");
            if (!json.trim().isEmpty()) {
                date = new Gson().fromJson(json, AvailableDate.class);
                start_time = date.getStart_time();
                end_time = date.getEnd_time();

                start.setTimeInMillis(start_time);
                end.setTimeInMillis(end_time);
            }


            String temp1 = requireContext().getResources().getString(R.string.are_you_sure_to_book_an_appointment_with_dr),
                    temp2 = args.getString("name", "Name"),
                    temp3 = requireContext().getResources().getString(R.string.on),
                    temp4 = args.getString("day", "Day"),
                    temp5 = requireContext().getResources().getString(R.string.from),
                    temp6 = (start.get(Calendar.HOUR) == 0 ? "12" : start.get(Calendar.HOUR))
                            + ":" + (start.get(Calendar.MINUTE) < 10 ? "0" + start.get(Calendar.MINUTE) : start.get(Calendar.MINUTE)),
                    temp7 = start.get(Calendar.AM_PM) == Calendar.AM ?
                            requireContext().getResources().getString(R.string.am)
                            : requireContext().getResources().getString(R.string.pm),
                    temp8 = requireContext().getResources().getString(R.string.to),
                    temp9 = (end.get(Calendar.HOUR) == 0 ? "12" : end.get(Calendar.HOUR))
                            + ":" + (end.get(Calendar.MINUTE) < 10 ? "0" + end.get(Calendar.MINUTE) : end.get(Calendar.MINUTE)),
                    temp10 = end.get(Calendar.AM_PM) == Calendar.AM ?
                            requireContext().getResources().getString(R.string.am)
                            : requireContext().getResources().getString(R.string.pm);

            SpannableStringBuilder ssb = new SpannableStringBuilder(temp1 + temp2 + temp3 + temp4 + temp5 + temp6 + temp7 + temp8 + temp9 + temp10 + requireContext().getResources().getString(R.string.question_mark));
            ForegroundColorSpan fcsPrimary = new ForegroundColorSpan(requireContext().getResources().getColor(R.color.primary));
            ForegroundColorSpan fcsPrimary2 = new ForegroundColorSpan(requireContext().getResources().getColor(R.color.primary));
            ForegroundColorSpan fcsPrimary3 = new ForegroundColorSpan(requireContext().getResources().getColor(R.color.primary));
            ForegroundColorSpan fcsPrimary4 = new ForegroundColorSpan(requireContext().getResources().getColor(R.color.primary));

            ssb.setSpan(fcsPrimary, temp1.length(), temp1.length() + temp2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(fcsPrimary2, temp1.length() + temp2.length() + temp3.length(), temp1.length() + temp2.length() + temp3.length() + temp4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(fcsPrimary3, temp1.length() + temp2.length() + temp3.length() + temp4.length() + temp5.length(), temp1.length() + temp2.length() + temp3.length() + temp4.length() + temp5.length() + temp6.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(fcsPrimary4, temp1.length() + temp2.length() + temp3.length() + temp4.length() + temp5.length() + temp6.length() + temp7.length() + temp8.length(), temp1.length() + temp2.length() + temp3.length() + temp4.length() + temp5.length() + temp6.length() + temp7.length() + temp8.length() + temp9.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            are_you_sure_question_text.setText(ssb);
        }
        view.findViewById(R.id.yes_send).setOnClickListener(v -> {
            if (phone.getEditText().getText().toString().trim().isEmpty()) {
                address.setError(null);
                phone.setError("Can't be empty");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                address.setError(null);
                phone.setError("Enter valid phone number");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (address.getEditText().getText().toString().trim().isEmpty()) {
                phone.setError(null);
                address.setError("Can't be empty");
                address.requestFocus();
                open_keyboard(address.getEditText());
            } else {
                sendOrderRequest(currentUser.getUid(),
                        currentAkhysai.getAkhysai_id(),
                        System.currentTimeMillis(),
                        phone.getEditText().getText().toString().trim(),
                        address.getEditText().getText().toString().trim(),
                        notes.getEditText().getText().toString().trim(),
                        start_time,
                        end_time);
                Navigation.findNavController(v).popBackStack(R.id.homeFragment, true);
            }
        });
        view.findViewById(R.id.no_cancel).setOnClickListener(v -> {
            close_keyboard();
            requireActivity().onBackPressed();
        });
        return view;
    }

    private void sendOrderRequest(String uid, String akhysai_id, long currentTimeMillis, String phone, String address, String notes, long start_time, long end_time) {

        Toast.makeText(requireContext(), "Successfully sent", Toast.LENGTH_SHORT).show();
    }

    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText

    }

    private void close_keyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}