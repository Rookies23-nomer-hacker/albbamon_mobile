package com.example.albbamon.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerUtils {

    // ✅ 공통 스피너 설정 메서드
    public static void setupSpinner(Context context, Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        // 선택 이벤트 추가
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 항목 처리 가능
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
