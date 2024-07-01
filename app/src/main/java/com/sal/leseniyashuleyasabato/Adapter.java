package com.sal.leseniyashuleyasabato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes3.dex */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<LessonModels> days;

    public Adapter(List<LessonModels> days) {
        this.days = days;
    }

    @NonNull
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View day = LayoutInflater.from(parent.getContext()).inflate(R.layout.wk_day_recyc, parent, false);
        return new ViewHolder(day);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        String Date = this.days.get(position).getDate();
        String Day_title = this.days.get(position).getDay_title();
        String Day_content = this.days.get(position).getDay_content();
        String Day_question = this.days.get(position).getDay_question();
        int Share_image = this.days.get(position).getShare_image();
        holder.setData(Date, Day_title, Day_content, Day_question, Share_image);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.days.size();
    }

    /* loaded from: classes3.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView day_content;
        private final TextView day_question;
        private final TextView day_title;
        private final ImageView share_image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.day_title = itemView.findViewById(R.id.day_title);
            this.day_content = itemView.findViewById(R.id.day_content);
            this.day_question = itemView.findViewById(R.id.day_question);
            this.share_image = itemView.findViewById(R.id.day_share);
        }

        public void setData(String Date, String Day_title, String Day_content, String Day_question, int Share_image) {
            this.date.setText(Date);
            this.day_title.setText(Day_title);
            this.day_content.setText(Day_content);
            this.day_question.setText(Day_question);
            this.share_image.setImageResource(Share_image);
        }
    }
}