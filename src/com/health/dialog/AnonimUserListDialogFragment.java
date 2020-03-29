package com.health.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.health.dependency.Dependency;
import com.health.dependency.DependencyGroup;
import com.health.dependency.DependencySet;
import com.health.main.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnonimUserListDialogFragment extends DialogFragment {
	
	public static final String EXTRA_ID = "id";

	public static AnonimUserListDialogFragment newInstance() {
        AnonimUserListDialogFragment f = new AnonimUserListDialogFragment();
        return f;
    }    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = 0;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_anonim_list, container, false);
        getDialog().setTitle("Выберите ознакомительный дневник");

        ListView listView = (ListView) v.findViewById(R.id.lv);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DependencySet.getDiaryTitles());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getIntent().putExtra(EXTRA_ID, position + 1);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int dependencyGroupId = position + 1;
                DependencyGroup dependencyGroup = DependencySet.getDependencyGroup(dependencyGroupId);
                if (dependencyGroup != null) {
                    Toast.makeText(getActivity(), dependencyGroup.getName(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        return v;
    }
}
