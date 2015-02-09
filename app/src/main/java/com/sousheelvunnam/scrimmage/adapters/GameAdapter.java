package com.sousheelvunnam.scrimmage.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
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

        final byte[] byteArray;
        if (game.has(ParseConstants.KEY_SCRIMMAGE_PICTURE)) {
            ParseFile file = game.getParseFile(ParseConstants.KEY_SCRIMMAGE_PICTURE);
            try {
                byteArray = file.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                holder.vLocationImage.setImageBitmap(bmp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            byteArray = null;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject scrimmage = game;

                Intent intent = new Intent(v.getContext(), ViewGameActivity.class);
                intent.putExtra("OBJECT_ID", scrimmage.getObjectId());
                intent.putExtra(ParseConstants.KEY_CREATOR_USERNAME, scrimmage.getString(ParseConstants.KEY_CREATOR_USERNAME));
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
