package com.sadicomputing.foodordering.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sadicomputing.foodordering.R;
import com.sadicomputing.foodordering.entity.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modykane on 11/12/2017.
 */

public class ViennoiseriesAdapter extends RecyclerView.Adapter<ViennoiseriesAdapter.ViewHolder> {

    private List<Article> mItems;
    private Context mContext;
    public static Long  idArticleSelected;
    Article article;
    int itemPosition;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView, textView2;
        private ImageView imageView, imageView2;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.designationplatsdujour);
            textView2 = itemView.findViewById(R.id.prixplatsdujour);
            imageView = itemView.findViewById(R.id.imageplatsdujour);
            imageView2 = itemView.findViewById(R.id.addplatsdujour);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Article item = getItem(getAdapterPosition());
            idArticleSelected=item.getIdArticle();
            notifyDataSetChanged();
            //Toast.makeText(mContext,item.getDesignation()+" est ajouté à la commande", Toast.LENGTH_SHORT).show();

        }
    }

    public ViennoiseriesAdapter(Context context, List<Article> articles) {
        mItems = articles;
        mContext = context;
    }

    @Override
    public ViennoiseriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.row_platsdujour, parent, false);
        return new ViennoiseriesAdapter.ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(final ViennoiseriesAdapter.ViewHolder holder, final int position) {
        Article item = mItems.get(position);
        holder.textView.setText(item.getDesignation());
        holder.textView2.setText(""+item.getPrix()+" FCFA");
        holder.imageView.setImageResource(R.drawable.icons8_cupcake);
        holder.imageView2.setImageResource(R.drawable.ic_action_add);

        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article = getItem(holder.getAdapterPosition());
                Toast.makeText(mContext,article.getDesignation()+" est ajouté à la commande", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Article> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Article getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public void setFilter(List<Article> items) {
        mItems = new ArrayList<>();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}