package tranthanh.dmt.gamecaro.pvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tranthanh.dmt.gamecaro.R;
import tranthanh.dmt.gamecaro.chuyendulieu;
import tranthanh.dmt.gamecaro.dong_thuoc_tinh;

public class custom_adapter extends RecyclerView.Adapter<custom_adapter.ViewHolder> {
    List<dong_thuoc_tinh> list;
    Context context;
    chuyendulieu ion;

    public custom_adapter(List<dong_thuoc_tinh> list, Context context,chuyendulieu ion) {
        this.list = list;
        this.context = context;
        this.ion=ion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adpater,parent,false);
        custom_adapter.ViewHolder viewHolder=new custom_adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (!list.get(position).isComputer()) {
                   list.get(position).setAnh(R.drawable.ic_clear_black_24dp); //Log.d("abc", "onBindViewHolder: "+list.get(position).getAnh());
                   ion.chuyendulieu(list.get(position).getRow(), list.get(position).getColum());
                   list.get(position).setComputer(true);
               }

           }
       });
        holder.img.setImageResource(list.get(position).getAnh());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgadapter);
        }
    }
}
