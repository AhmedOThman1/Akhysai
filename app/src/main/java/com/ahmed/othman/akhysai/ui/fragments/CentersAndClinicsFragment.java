package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ClinicAdapter;
import com.ahmed.othman.akhysai.pojo.Clinic;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.DirectoryCategoriesString;


public class CentersAndClinicsFragment extends Fragment {

    public CentersAndClinicsFragment() {
        // Required empty public constructor
    }

    TextInputLayout search;
    Spinner search_category;
    RecyclerView clinics_recycler;
    ArrayList<Clinic> ClinicsArray = new ArrayList<>();
    TextView clinics_text;
    NestedScrollView nested_scroll;
    ConstraintLayout no_result, no_internet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centers_and_clinics, container, false);
        search = view.findViewById(R.id.search);
        search_category = view.findViewById(R.id.search_category);

        clinics_recycler = view.findViewById(R.id.clinics_recycler);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        clinics_text = view.findViewById(R.id.akhysai_library_text);
        no_result = view.findViewById(R.id.no_result);
        no_internet = view.findViewById(R.id.no_internet);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.centers_and_clinics);

        ClinicAdapter clinicAdapter = new ClinicAdapter(requireContext());
        ClinicsArray = getAllClinics();

        clinicAdapter.setModels(ClinicsArray);
        clinics_recycler.setAdapter(clinicAdapter);
        clinics_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        clinics_recycler.setHasFixedSize(true);

        clinics_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), clinics_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", ClinicsArray.get(position).getClinic_id());
                bundle.putString("clinic", new Gson().toJson(ClinicsArray.get(position)));
                Navigation.findNavController(v).navigate(R.id.action_centersAndClinicsFragment_to_oneClinicFragment, bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, DirectoryCategoriesString);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_category.setAdapter(category_adapter);


        view.findViewById(R.id.clinic_search_button).setOnClickListener(v -> {
            if (search.getEditText().getText().toString().trim().isEmpty()) {
                search.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                search.requestFocus();
                open_keyboard(search.getEditText());
            } else if (search_category.getSelectedItemPosition() == 0) {
                search.setError(null);
                search.clearFocus();
                Toast.makeText(getContext(), "select category", Toast.LENGTH_SHORT).show();
                search_category.setBackgroundResource(R.drawable.background_spinner_error);
            } else {
                search.setError(null);
                search_category.setBackgroundResource(R.drawable.background_spinner);
                nested_scroll.smoothScrollTo(0, clinics_text.getTop());
                ClinicsArray = getClinicsByTitle(search.getEditText().getText().toString().trim(), DirectoryCategoriesString.get(search_category.getSelectedItemPosition()));
            }
        });


        return view;
    }


    private ArrayList<Clinic> getAllClinics() {
        ArrayList<Clinic> tempClinics = new ArrayList<>();
        tempClinics.add(new Clinic("https://www.akhysai.com/upload/directory/bccb2c10-4fb2-42e6-8312-47cb8a448f4f.jpg", "مؤسسة عطاء للطفولة", "ذوي احتياجات خاصة", "01097654241", "www.Akhysai.com", "Al-3ata", "اختبارات ومقاييس نفسية - تقييمات لحالات ذوى الاحتياجات الخاصة - جلسات تربية خاصة -جلسات تخاطب - جلسات تعديل سلوك"));
        tempClinics.add(new Clinic("0", "موقع الصفا للمستلزمات الطبية", "ذوي احتياجات خاصة", "01097654241", "www.Akhysai.com", "Al-Hoda", "يقول مونتاني في مقدمة كتابه (محاولات أو تجارب) : «إن هذا الكتاب حسن الطوية فهو ينبهك منذ البداية إني لا أستهدف من ورائه مقصداً إلا ما ينفع العام والخاص، ولم أرد به خدمتك أو إعلاء ذكرى فإن مواهبي تعجز عن تحقيق مثل هذه الغاية... لقد خصصته لمنفعة الخاصة من أهلي وأصدقائي حتى إذا ما افتقدوني استطاعوا أن يجدوا فيه صورة لطباعي وميولي، فيسترجعوا ذكراي التي خلفتها لهم حيّة كاملة ولو كان هدفي أن أظفر بإعجاب العالم لعملت على إطراء نفسي وإظهارها بطريقة منمّقة ولكني أريد أن أعرف في أبسط صوري الطبيعية العادية دون تكلف ولا تصنع لأني أنا الذي أصوّر نفسي لهذا تبرز مساوئي واضحة وسجيتي على طبيعتها ما سمح لي العرف بذلك...» يتضح في مقدمة كتاب ابن الجوزي صيد الخاطر إنما كتب هذه الفصول ليسجّل فيها خواطره التي أثارتها تجاربه وعلاقاته مع الأشياء. وهذه الخواطر ليست وليدة البحث والدرس العميق وإنما هي خواطر آنية تولد وتزول سريعاً إنْ لم تُدوّن لهذا سعى إلى تدوينها في هذا الكتاب وسمّاه (صيد الخاطر) كما سمّى فيما بعد أحمد أمين أشهر كتاب في المقالة الأدبية في الأدب العربي الحديث (فيض الخاطر) وهذا يعني أنَّ مفهوم ابن الجوزي لفصول كتابه قريب من مفهوم مونتاني لفصوله فهو جسّد فيها خواطره معلّقاً على هذا القول أو ذاك ومصوراً تجارب نفسه وعيوبها وما توصل إليه من أفكار تتعلق بالدين والحياة والمجتمع. يقول ابن الجوزي في مقدمة (صيد الخاطر): «. لَمّا كانت الخواطر تجول في تصفح أشياء تعرض لها، ثم تعرض عنها فتذهب، كان من أولى الأمور حفظ ما يخطر لكي لا ينسى، وقد قال عليه الصلاة والسلام: \"قيِّدوا العلم بالكتابة\". وكم خطر لي شيء فأتشاغل عن إثباته فيذهب، فأتأسف عليه ورأيت في نفسي إنني كلما فتحت بصر التفكر، سنح له من عجائب الغيب ما لم يكن في حساب فانثال عليه من كثيب التفهيم ما لا يجوز التفريط فيه فجعلت هذا الكتاب قيداً –لصيد الخاطر- والله وليّ النفع، إنه قريب مجيب»"));
        tempClinics.add(new Clinic("2", "عيادة الصفا", "الطب", "01127271078", "www.Akhysai.com", "Al-Safa", "تفاصيل الصفا"));
        tempClinics.add(new Clinic("3", "عيادة الفجر", "الطب", "01027777778", "www.Akhysai.com", "Al-Fajr", "تفاصيل الفجر"));
        tempClinics.add(new Clinic("1", "عيادة الهدي", "الطب", "01097654241", "www.Akhysai.com", "Al-Hoda", "تفاصيل الهدي"));
        return tempClinics;
    }

    private ArrayList<Clinic> getClinicsByTitle(String title, String category) {
        ArrayList<Clinic> tempClinics = new ArrayList<>();
        tempClinics.add(new Clinic("1", "عيادة الهدي", "الطب", "01097654241", "www.Akhysai.com", "Al-Hoda", "تفاصيل الهدي"));
        tempClinics.add(new Clinic("-1", title, category, "01027777778", "www.Akhysai.com", "Al-Fajr", "تفاصيل الفجر"));
        tempClinics.add(new Clinic("0", "موقع الصفا للمستلزمات الطبية", "ذوي احتياجات خاصة", "01097654241", "www.Akhysai.com", "Al-Hoda", "يقول مونتاني في مقدمة كتابه (محاولات أو تجارب) : «إن هذا الكتاب حسن الطوية فهو ينبهك منذ البداية إني لا أستهدف من ورائه مقصداً إلا ما ينفع العام والخاص، ولم أرد به خدمتك أو إعلاء ذكرى فإن مواهبي تعجز عن تحقيق مثل هذه الغاية... لقد خصصته لمنفعة الخاصة من أهلي وأصدقائي حتى إذا ما افتقدوني استطاعوا أن يجدوا فيه صورة لطباعي وميولي، فيسترجعوا ذكراي التي خلفتها لهم حيّة كاملة ولو كان هدفي أن أظفر بإعجاب العالم لعملت على إطراء نفسي وإظهارها بطريقة منمّقة ولكني أريد أن أعرف في أبسط صوري الطبيعية العادية دون تكلف ولا تصنع لأني أنا الذي أصوّر نفسي لهذا تبرز مساوئي واضحة وسجيتي على طبيعتها ما سمح لي العرف بذلك...» يتضح في مقدمة كتاب ابن الجوزي صيد الخاطر إنما كتب هذه الفصول ليسجّل فيها خواطره التي أثارتها تجاربه وعلاقاته مع الأشياء. وهذه الخواطر ليست وليدة البحث والدرس العميق وإنما هي خواطر آنية تولد وتزول سريعاً إنْ لم تُدوّن لهذا سعى إلى تدوينها في هذا الكتاب وسمّاه (صيد الخاطر) كما سمّى فيما بعد أحمد أمين أشهر كتاب في المقالة الأدبية في الأدب العربي الحديث (فيض الخاطر) وهذا يعني أنَّ مفهوم ابن الجوزي لفصول كتابه قريب من مفهوم مونتاني لفصوله فهو جسّد فيها خواطره معلّقاً على هذا القول أو ذاك ومصوراً تجارب نفسه وعيوبها وما توصل إليه من أفكار تتعلق بالدين والحياة والمجتمع. يقول ابن الجوزي في مقدمة (صيد الخاطر): «. لَمّا كانت الخواطر تجول في تصفح أشياء تعرض لها، ثم تعرض عنها فتذهب، كان من أولى الأمور حفظ ما يخطر لكي لا ينسى، وقد قال عليه الصلاة والسلام: \"قيِّدوا العلم بالكتابة\". وكم خطر لي شيء فأتشاغل عن إثباته فيذهب، فأتأسف عليه ورأيت في نفسي إنني كلما فتحت بصر التفكر، سنح له من عجائب الغيب ما لم يكن في حساب فانثال عليه من كثيب التفهيم ما لا يجوز التفريط فيه فجعلت هذا الكتاب قيداً –لصيد الخاطر- والله وليّ النفع، إنه قريب مجيب»"));
        tempClinics.add(new Clinic("1", "عيادة الهدي", "الطب", "01097654241", "www.Akhysai.com", "Al-Hoda", "تفاصيل الهدي"));
        tempClinics.add(new Clinic("2", "عيادة الصفا", "الطب", "01127271078", "www.Akhysai.com", "Al-Safa", "تفاصيل الصفا"));
        return tempClinics;
    }


    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
//                NavOptions navOptions = new NavOptions.Builder()
//                        .setPopUpTo(R.id.homeFragment, true)
//                        .setEnterAnim(R.anim.slide_in_right)
//                        .setExitAnim(R.anim.slide_out_left)
//                        .setPopEnterAnim(R.anim.slide_in_left)
//                        .setPopExitAnim(R.anim.slide_out_right)
//                        .build();
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}