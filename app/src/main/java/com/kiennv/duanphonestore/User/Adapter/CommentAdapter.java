package com.kiennv.duanphonestore.User.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.Comment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentAdapter(Context context,List<Comment> commentList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.commentList=commentList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Comment comment=commentList.get(position);
        holder.txtName.setText( comment.getNameUS());
        holder.txtStatus.setText(comment.getStatus());
        Picasso.get().load(comment.getImageUS()).into(holder.imageViewAVT);

    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageViewAVT;
        TextView txtName,txtStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtTenCM);
            imageViewAVT=itemView.findViewById(R.id.imgUSCM);
            txtStatus=itemView.findViewById(R.id.txtSTT);
        }
    }
}
