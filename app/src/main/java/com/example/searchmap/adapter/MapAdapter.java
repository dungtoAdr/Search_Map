package com.example.searchmap.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchmap.R;
import com.example.searchmap.listener.ItemClickListener;
import com.example.searchmap.model.Map;
import com.example.searchmap.model.Position;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {
    private List<Map> list;
    private Activity activity;
    private Position currentUserPosition;
    private String searchKeyword;
    String title;

    public MapAdapter(List<Map> list, Activity activity, Position currentUserPosition, String searchKeyword) {
        this.list = list;
        this.activity = activity;
        this.currentUserPosition = currentUserPosition;
        this.searchKeyword = searchKeyword;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_map, parent, false);
        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        title = list.get(position).getTitle();
        holder.textView.setText(title);
        HighLightKeyword(holder);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Position position1 = list.get(position).getPosition();
                String url = "https://www.google.com/maps/dir/?api=1&origin="
                        + currentUserPosition.getLat() + "," + currentUserPosition.getLng()
                        + "&destination=" + position1.getLat() + "," + position1.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ItemClickListener itemClickListener;

        public MapViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public void HighLightKeyword(@NonNull MapViewHolder holder) {
        String normalizedTitle = StringUtils.removeDiacritics(title);
        String normalizedKeyword = StringUtils.removeDiacritics(searchKeyword);
        SpannableString spannableString = new SpannableString(title);
        if (normalizedKeyword != null && !normalizedKeyword.isEmpty()) {
            int startIndex = normalizedTitle.toLowerCase().indexOf(normalizedKeyword.toLowerCase());
            if (startIndex != -1) {
                spannableString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.highlightColor)),
                        startIndex, startIndex + searchKeyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        holder.textView.setText(spannableString);
    }

    public static class StringUtils {
        public static String removeDiacritics(String s) {
            String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(normalized).replaceAll("");
        }
    }
}
