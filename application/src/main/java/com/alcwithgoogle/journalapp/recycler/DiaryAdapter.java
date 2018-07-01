package com.alcwithgoogle.journalapp.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcwithgoogle.journalapp.R;
import com.alcwithgoogle.journalapp.model.Diary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private static final String DATE_FORMAT = "dd-mm-yy";

    private SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(DATE_FORMAT,Locale.getDefault());

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;

    private List<Diary> diaries;
    private final Context mContext;

    public DiaryAdapter(Context context,ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diaries_layout,parent,false);

        return new DiaryViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        holder.diaryTitle.setText(diaries.get(position).getTitle());
        holder.diaryDescription.setText(diaries.get(position).getDescription());
        // format the date
        Date date = new Date();
        String _date = simpleDateFormat.format(date);
        holder.diaryDate.setText(_date);
    }

    /**
     * When data changes, this method updates the list of diaries
     * and notifies the adapter to use the new values on it
     */
    public void setDiaries(List<Diary> diariesList) {
        diaries = diariesList;
        notifyDataSetChanged();
    }

    // Item Click listener interface
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // onlong click listener
    public interface LongClickListener {
        void onLongListener();
    }

    public List<Diary> getDiaries() {
        return diaries;
    }

    @Override
    public int getItemCount() {
        if (diaries == null) {
            return 0;
        }
        return diaries.size();
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {

        TextView diaryTitle;
        TextView diaryDescription;
        TextView diaryDate;
        private CardView diaryCard;

        private DiaryViewHolder(final View itemView) {
            super(itemView);
            diaryTitle = itemView.findViewById(R.id.diary_title);
            diaryDescription = itemView.findViewById(R.id.diary_description);
            diaryDate = itemView.findViewById(R.id.diary_date);

            diaryCard = itemView.findViewById(R.id.diary_card);
            diaryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClickListener(diaries.get
                            (getAdapterPosition()).getId());
                }
            });
        }
    }
}
