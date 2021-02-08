package com.hjhj.daedan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomsheetDialog extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_bottomsheet, container, false);

        RadioGroup rg = v.findViewById(R.id.bottomsheet_rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checked = v.findViewById(radioGroup.getCheckedRadioButtonId());
                String checkedCategory = checked.getText().toString();
                Intent intent = new Intent(getActivity(),Tab1_Map_WriteActivity.class);
                intent.putExtra("category",checkedCategory);
                Log.d("INTENT",checkedCategory);
                startActivity(intent);

            }
        });


        return v;
    }
}

