package com.sousheelvunnam.scrimmage.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Random;

/**
 * Created by Sousheel on 12/23/2014.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<ParseObject> gameList;
    private Context gameActivityContext;
    private int mImageResourceID;

    public GameAdapter (List<ParseObject> myGameList, Context context) {
        gameList = myGameList;
        gameActivityContext = context;
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

        /*final byte[] byteArray;
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
        }*/
        mImageResourceID = getImage(game.getString(ParseConstants.KEY_SCRIMMAGE_SPORT));
        final Bitmap bitmap = BitmapFactory.decodeResource(gameActivityContext.getResources(), mImageResourceID);
        holder.vLocationImage.setImageBitmap(bitmap);

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
                intent.putExtra(ParseConstants.KEY_SCRIMMAGE_PICTURE, mImageResourceID);

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
    private int getImage(String sport) {
        int bitmapResourceID = ParseConstants.SCRIMMAGE_LOGO_RESOURCE_ID;
        Random randy = new Random();
        int imageNumber;

        if (sport.equalsIgnoreCase("Soccer")) {
            imageNumber = randy.nextInt(7);
            if (imageNumber ==  0) { bitmapResourceID = R.drawable.soccer;}
            else if (imageNumber ==  1) { bitmapResourceID = R.drawable.soccer3;}
            else if (imageNumber ==  2) { bitmapResourceID = R.drawable.soccer4;}
            else if (imageNumber ==  3) { bitmapResourceID = R.drawable.soccer2;}
            else if (imageNumber ==  4) { bitmapResourceID = R.drawable.soccer5;}
            else if (imageNumber ==  5) { bitmapResourceID = R.drawable.soccer6;}
            else if (imageNumber ==  6) { bitmapResourceID = R.drawable.soccer7;}
        }
        else if (sport.equalsIgnoreCase("Football")) {
            imageNumber = randy.nextInt(4);
            if (imageNumber ==  0) { bitmapResourceID = R.drawable.football;}
            else if (imageNumber ==  1) { bitmapResourceID = R.drawable.football2;}
            else if (imageNumber ==  2) { bitmapResourceID = R.drawable.football3;}
            else if (imageNumber ==  3) { bitmapResourceID = R.drawable.football4;}
        }
        else if (sport.equalsIgnoreCase("Running")) {
            imageNumber = randy.nextInt(4);
            if (imageNumber ==  0) { bitmapResourceID = R.drawable.running;}
            else if (imageNumber ==  1) { bitmapResourceID = R.drawable.running2;}
            else if (imageNumber ==  2) { bitmapResourceID = R.drawable.running3;}
            else if (imageNumber ==  3) { bitmapResourceID = R.drawable.running4;}
        }
        else if (sport.equalsIgnoreCase("Basketball")) {
            imageNumber = randy.nextInt(3);
            if (imageNumber ==  0) { bitmapResourceID = R.drawable.basketball;}
            else if (imageNumber ==  1) { bitmapResourceID = R.drawable.basketball3;}
            else if (imageNumber ==  2) { bitmapResourceID = R.drawable.basketball2;}
        }
        else if (sport.equalsIgnoreCase("Workout")) {
            imageNumber = randy.nextInt(4);
            if (imageNumber ==  0) { bitmapResourceID = R.drawable.workout;}
            else if (imageNumber ==  1) { bitmapResourceID = R.drawable.workout2;}
            else if (imageNumber ==  2) { bitmapResourceID = R.drawable.workout3;}
            else if (imageNumber ==  3) { bitmapResourceID = R.drawable.workout4;}
        }

        return bitmapResourceID;
    }
}
