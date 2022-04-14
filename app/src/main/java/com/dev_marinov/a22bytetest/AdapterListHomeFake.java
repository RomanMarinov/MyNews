package com.dev_marinov.a22bytetest;

import android.content.Context;
import android.graphics.Point;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterListHomeFake extends RecyclerView.Adapter<AdapterListHomeFake.ViewHolder> {


    //public MyInterfaceGetPositionUrl myInterfaceGetPositionUrl;
    int[] lastVisibleItemPositions;

    Context context;
    ArrayList<String> arrayList;
    int width;

    public AdapterListHomeFake(Context context, ArrayList<String> arrayList, RecyclerView recyclerView) {
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
    public AdapterListHomeFake.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_list_fake, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListHomeFake.ViewHolder holder, int position) {


        holder.head.setText(arrayList.get(position).toString()); // -----> заголовок
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("676","click go to Webview");

                        ((MainActivity)context).flipCard("goWebviewActivity", "https://yandex.ru/");



                    }
                });


//                Intent intent = new Intent(context, WebviewActivity.class);
//                intent.putExtra("url", arrayList.get(position).getUrl());
//                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
      //  Log.e("333","-arrayList.size()-" + arrayList.size());
        return arrayList == null ? 0 : arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView head;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            head = itemView.findViewById(R.id.tvHead);
        }
    }



}
