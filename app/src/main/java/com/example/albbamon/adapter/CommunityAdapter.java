package com.example.albbamon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import com.example.albbamon.R;
import com.example.albbamon.model.CommunityModel;
import java.util.List;

public class CommunityAdapter extends ArrayAdapter<CommunityModel> {
    private Context context;
    private List<CommunityModel> communityList;

    public CommunityAdapter(Context context, List<CommunityModel> communityList) {
        super(context, 0, communityList);
        this.context = context;
        this.communityList = communityList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_community, parent, false);
        }
        Log.e("keyword", "검색 제목: " + R.id.item_title);
        // View 참조
        TextView titleView = convertView.findViewById(R.id.item_title);
        TextView contentView = convertView.findViewById(R.id.item_content);
        TextView userView = convertView.findViewById(R.id.item_user);
        TextView dateView = convertView.findViewById(R.id.item_date);

        // 현재 위치의 데이터 가져오기
        CommunityModel community = communityList.get(position);

        // 데이터 설정
        titleView.setText(community.getTitle());
        contentView.setText(community.getContents());
        userView.setText(community.getUserName());
        dateView.setText(community.getCreateDate().substring(0, 10)); // 날짜만 표시

        return convertView;
    }

}
