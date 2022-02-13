package com.samim.bbcnewsdemoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<BBCNewsClass> newsClass;
    Context context;

    Adapter(Context context, List<BBCNewsClass> newsClass){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.newsClass = newsClass;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //data received
        BBCNewsClass news = newsClass.get(position);

        holder.txtTitle.setText(news.title);

        holder.txtDesc.setText(news.description);

        holder.txtDate.setText(news.publishedAt);

        String imageUrl = news.urlToImage;

        Glide.with(context)
                .load(imageUrl.trim())
                .placeholder(R.drawable.bbc)
                .error(R.drawable.bbc)
                .override(130, 100)
                .into(holder.imageView);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int item = holder.getAbsoluteAdapterPosition();
                BBCNewsClass selectedNews = newsClass.get(item);
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("title", selectedNews.title);
                intent.putExtra("author", selectedNews.author);
                intent.putExtra("desc", selectedNews.description);
                intent.putExtra("date", selectedNews.publishedAt);
                intent.putExtra("content", selectedNews.content);
                intent.putExtra("image", selectedNews.urlToImage);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsClass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDesc, txtDate;
        ImageView imageView;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.n_title);
            txtDesc = itemView.findViewById(R.id.n_desc);
            txtDate = itemView.findViewById(R.id.n_date);
            imageView  = itemView.findViewById(R.id.imageView3);
            mainLayout = itemView.findViewById(R.id.custom_layout);
        }
    }
}
