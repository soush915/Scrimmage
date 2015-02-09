package com.sousheelvunnam.scrimmage.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.ui.ViewGameActivity;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.List;

/**
 * Created by Sousheel on 1/29/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<ParseObject> gameList;

    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list, parent, false);
        return new HomeViewHolder(v);
    }

    public HomeAdapter(List<ParseObject> myGameList) {
        gameList = myGameList;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        //Put data from parse objects in here and set text for textviews
        final ParseObject game = gameList.get(position);

        holder.vTitle.setText(game.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
        holder.vInfo.setText(game.getString(ParseConstants.KEY_FAVORITE_SPORT) + " - " + game.getDate(ParseConstants.KEY_SCRIMMAGE_DATE).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject scrimmage = game;

                Intent intent = new Intent(v.getContext(), ViewGameActivity.class);
                intent.putExtra("OBJECT_ID", scrimmage.getObjectId());
                intent.putExtra(ParseConstants.KEY_CREATOR_USERNAME, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_TITLE, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DATE, scrimmage.getDate(ParseConstants.KEY_SCRIMMAGE_DATE).getTime());
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_SPORT, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_SPORT));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_ADDRESS, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_ADDRESS));

                if (game.has(ParseConstants.KEY_SCRIMMAGE_PICTURE)) {
                    try {
                        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_PICTURE, scrimmage.getParseFile(ParseConstants.KEY_SCRIMMAGE_PICTURE).getData());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImage;
        protected TextView vTitle;
        protected TextView vInfo;

        public HomeViewHolder(View v) {
            super(v);
            vImage = (ImageView) v.findViewById(R.id.sportListImageView);
            vTitle =  (TextView) v.findViewById(R.id.titleListTextView);
            vInfo = (TextView)  v.findViewById(R.id.infoListTextView);
        }
    }
}