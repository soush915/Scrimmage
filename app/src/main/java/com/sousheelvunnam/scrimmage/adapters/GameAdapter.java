package com.sousheelvunnam.scrimmage.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.ui.ViewGameActivity;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.List;

/**
 * Created by Sousheel on 12/23/2014.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<ParseObject> gameList;

    public GameAdapter (List<ParseObject> myGameList) {
        gameList = myGameList;
    }

    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        return new GameViewHolder(v);
    }

    /**
     * When data binds to view
     * When data is shown in UI
     */
    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        //Put data from parse objects in here and set text for textviews
        final ParseObject game = gameList.get(position);

        holder.vTitle.setText(game.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
        holder.vDescription.setText(game.getString(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject scrimmage = game;

                Intent intent = new Intent(v.getContext(), ViewGameActivity.class);
                //intent.putExtra("OBJECT_ID", scrimmage.getObjectId());
                intent.putExtra(ParseConstants.KEY_CREATOR_USERNAME, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_TITLE, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DAY, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DAY));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_MONTH, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_MONTH));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_YEAR, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_YEAR));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_MINUTE, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_MINUTE));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_HOUR, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_HOUR));
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_SPORT, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_SPORT));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vLocationImage;
        protected TextView vTitle;
        protected TextView vDescription;
        protected ImageView vGoingImage1;

        public GameViewHolder(View v) {
            super(v);
            vLocationImage = (ImageView) v.findViewById(R.id.locationImage);
            vTitle =  (TextView) v.findViewById(R.id.titleTextView);
            vDescription = (TextView)  v.findViewById(R.id.descriptionCardTextView);
            vGoingImage1 = (ImageView) v.findViewById(R.id.goingImageView1);
        }
    }
}
