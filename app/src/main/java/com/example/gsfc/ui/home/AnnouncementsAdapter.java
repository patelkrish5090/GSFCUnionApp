package com.example.gsfc.ui.home;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
import com.example.gsfc.R;
import com.example.gsfc.ui.home.Announcement;
import com.google.firebase.database.DatabaseReference;
//
//import java.util.List;
//
//public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder> {
//
//    private List<Announcement> announcements;
//    private OnAnnouncementClickListener listener;
//    private DatabaseReference myRefAnnouncements;
//
//    public AnnouncementsAdapter(List<Announcement> announcements, DatabaseReference myRefAnnouncements) {
//        this.announcements = announcements;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcements, parent, false);
//        return new AnnouncementViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
//        Announcement announcement = announcements.get(position);
//        holder.title.setText(announcement.getTitle());
//        holder.details.setText(announcement.getDetails());
//        holder.date.setText(announcement.getDate());
//
//        // Set click listener on the card view
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
////        String url = announcement.getLink();
////        if (url != null && !url.isEmpty() && !url.equals("none")) {
////            holder.linkButton.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
////                    holder.itemView.getContext().startActivity(intent);
////                }
////            });
////        } else {
////            holder.linkButton.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Toast.makeText(holder.itemView.getContext(), "No file attached to this", Toast.LENGTH_SHORT).show();
////                }
////            });
////        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return announcements.size();
//    }
//
//    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
//        TextView title;
//        TextView details;
//        TextView date;
//        Button linkButton;
//
//        public AnnouncementViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.announcement_title);
//            date = itemView.findViewById(R.id.announcement_date);
//        }
//    }
//
//    public interface OnAnnouncementClickListener {
//        void onAnnouncementClicked(Announcement user);
//    }
//}
//

//package com.example.gsfc.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsfc.AnnouncementViewActivity;
import com.example.gsfc.R;
import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcements;
    private DatabaseReference myRefAnnouncement;
    private OnItemClickListener listener;

    public AnnouncementsAdapter(List<Announcement> announcements, DatabaseReference myRefAnnouncement) {
        this.announcements = announcements;
        this.myRefAnnouncement = myRefAnnouncement;
    }

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcements, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.titleTextView.setText(announcement.getTitle());
        holder.dateTextView.setText(announcement.getDate());

        // Set the click listener
        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, AnnouncementViewActivity.class);
            intent.putExtra("title", announcement.getTitle());
            intent.putExtra("date", announcement.getDate());
            intent.putExtra("details", announcement.getDetails());
            intent.putExtra("attachmentUrl", announcement.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView dateTextView;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.announcement_title);
            dateTextView = itemView.findViewById(R.id.announcement_date);
        }
    }
}
