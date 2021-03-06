package com.dev_marinov.a22bytetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterListHome extends RecyclerView.Adapter<AdapterListHome.ViewHolder> {

    int[] lastVisibleItemPositions;

    Context context;
    ArrayList<ModelClass> arrayList;
    int width;

    public AdapterListHome(Context context, ArrayList<ModelClass> arrayList, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;

        Display display = ((MainActivity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        int height = size.y;

        final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPositions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
                //Log.e("zzz","-lastVisibleItemPositions=" + lastVisibleItemPositions.length);

                ((MainActivity)context).lastVisibleItemHome = getMaxPosition(lastVisibleItemPositions);
                Log.e("zzz+","-getMaxPosition=" + getMaxPosition(lastVisibleItemPositions));

            }

            private int getMaxPosition(int[] positions) {
                int max = positions[0];
                return max;
            }
        });
    }

    @NonNull
    @Override
    public AdapterListHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_list, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListHome.ViewHolder holder, int position) {
       // Log.e("333","-holder-" + holder + "-position-" + position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("676","click imgSearch");
                        ((MainActivity)context).flipCard("goWebviewActivity", arrayList.get(position).getUrl());
                        Log.e("676","click arrayList.get(position).getUrl()" + arrayList.get(position).getUrl());
                    }
                });
            }
        });
        // ?????????????????????? VIEW ?????? ?????? ?? ????????????
        // ?????????????????? ???????????? ??????????????????
        holder.head.setText(arrayList.get(position).getTitle()); // -----> ??????????????????

        // ?????????????????? ????????????????
        if(arrayList.get(position).getUrlToImage() != null && (!arrayList.get(position).getUrlToImage().equals("")) )
        {
            Picasso.get()
                    .load(arrayList.get(position).getUrlToImage()).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(width, 0).centerCrop(Gravity.TOP)
                    .into(holder.imageView);  // -----> ????????????????
        }
        else
        {
            holder.imageView.setVisibility(View.GONE);
        }

        if(arrayList.get(position).getDescription() != null)
        {
            holder.content.setText(arrayList.get(position).getDescription()); // -----> ????????????????
        }
        else
        {
            holder.content.setVisibility(View.GONE);
        }

        // ?????????????????? ????????????. ???????? ?????????? ??????, ???? view - gone
        if(arrayList.get(position).getAuthor() != null)
        {
            holder.author.setText("??????????: " + arrayList.get(position).getAuthor()); // -----> ??????????
        }
        else
        {
            holder.author.setVisibility(View.GONE);
        }
//        Log.e("adapter","-arrayListArticles.get(position).getAuthor()-" + arrayListArticles.get(position).getAuthor());
        // ?????????????????? ???????????? ????????
        holder.time.setText("????????????????????????: " + arrayList.get(position).getPublishedAt()); // -----> ????????
    }

    @Override
    public int getItemCount() {
      //  Log.e("333","-arrayList.size()-" + arrayList.size());
        return arrayList == null ? 0 : arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView head, content, author, time;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.img);

            head = itemView.findViewById(R.id.tvHead);
            content = itemView.findViewById(R.id.tvContent);
            author = itemView.findViewById(R.id.tvAuthor);
            time = itemView.findViewById(R.id.tvTime);
        }
    }



}
