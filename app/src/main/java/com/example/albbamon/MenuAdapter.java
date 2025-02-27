package com.example.albbamon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuModel> menuList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MenuAdapter(List<MenuModel> menuList, OnItemClickListener listener) {
        this.menuList = menuList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuModel menu = menuList.get(position);
        holder.menuText.setText(menu.getTitle());
        holder.menuIcon.setImageResource(menu.getIconResId());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menuText;
        ImageView menuIcon;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuText = itemView.findViewById(R.id.menu_text);
            menuIcon = itemView.findViewById(R.id.menu_icon);
        }
    }
}
