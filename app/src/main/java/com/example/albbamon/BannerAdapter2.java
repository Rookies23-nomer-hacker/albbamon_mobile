package com.example.albbamon;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class BannerAdapter2 extends RecyclerView.Adapter<BannerAdapter2.BannerViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public BannerAdapter2(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // ✅ 기본 이미지가 리소스 ID로 들어오면 Glide에서 `Uri` 형식으로 로드
        if (imageUrl.startsWith("android.resource://")) {
            Glide.with(context)
                    .load(Uri.parse(imageUrl))
                    .into(holder.bannerImage);
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.b_logo) // ✅ 로딩 중 기본 이미지
                    .error(R.drawable.b_logo) // ✅ 에러 시 기본 이미지
                    .into(holder.bannerImage);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.banner_image);
        }
    }
}
