package com.citiexchangeplatform.pointsleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.citiexchangeplatform.pointsleague.AddCardActivity;
import com.citiexchangeplatform.pointsleague.CardInfoActivity;
import com.citiexchangeplatform.pointsleague.R;
import com.citiexchangeplatform.pointsleague.models.CardPointsModel;

import java.util.ArrayList;
import java.util.List;

public class CardPointsAdapter extends RecyclerView.Adapter {
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView textViewName;
        public final TextView textViewPoints;
        public final TextView textViewExchangePoints;
        public final ImageView imageView;

        public VH(View v) {
            super(v);
            textViewName = (TextView) v.findViewById(R.id.textView_name_card_point_item);
            textViewPoints = (TextView) v.findViewById(R.id.textView_points_card_point_item);
            textViewExchangePoints = (TextView) v.findViewById(R.id.textView_exchange_points_card_point_item);
            imageView = (ImageView) v.findViewById(R.id.imageView_card_point_item);
        }
    }

    public static class VHEmpty extends RecyclerView.ViewHolder{

        public VHEmpty(View v) {
            super(v);
        }
    }

    private final int EMPTY_VIEW_TYPE = -1;

    private Context context;
    private List<CardPointsModel> items;

    public CardPointsAdapter(Context context) {
        items = new ArrayList<CardPointsModel>();
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() <= 0) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardPointsAdapter.VH){
            ((CardPointsAdapter.VH)holder).textViewName.setText(items.get(position).getName());
            ((CardPointsAdapter.VH)holder).textViewPoints.setText(String.valueOf(items.get(position).getPoints()));
            ((CardPointsAdapter.VH)holder).textViewExchangePoints.setText(String.format("%.1f",items.get(position).getPoints()/items.get(position).getProportion()));
            Glide.with(context)
                    .load(items.get(position).getCardLogoURL())
                    .centerCrop()
                    .placeholder(R.drawable.loading_card)
                    .error(R.drawable.loading_card)
                    .into(((CardPointsAdapter.VH)holder).imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToCardInfo = new Intent(context, CardInfoActivity.class);
                    intentToCardInfo.putExtra("merchantID",items.get(position).getMerchantID());
                    context.startActivity(intentToCardInfo);
                }
            });
        }else if (holder instanceof CardPointsAdapter.VHEmpty){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToAddCard = new Intent(context, AddCardActivity.class);
                    context.startActivity(intentToAddCard);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size()<=0? 1 : items.size() ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (EMPTY_VIEW_TYPE == viewType) {
            view = inflater.inflate(R.layout.item_card_points_empty, parent, false);
            return new CardPointsAdapter.VHEmpty(view);
        }else {
            view = inflater.inflate(R.layout.item_card_points, parent, false);
            return new CardPointsAdapter.VH(view);
        }
    }


    public void addData(String name, String merchantName, int point, double proportion, String cardLogoURL) {
        CardPointsModel newItem = new CardPointsModel(name, merchantName, point, proportion, cardLogoURL);
        items.add(newItem);
        notifyDataSetChanged();
    }

}