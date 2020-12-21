package com.ccube9.gochat.Home.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ccube9.gochat.Home.Fragment.Request_for_winner;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.WinnerList;

import com.ccube9.gochat.WinnerInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class RequestWinnerAdapter extends ArrayAdapter<WinnerList> {


    private List<WinnerList> myFriends;

    private Activity activity;
    private int selectedPosition = -1;
    WinnerInterface winnerInterface;




    public RequestWinnerAdapter(Activity context, int resource, List<WinnerList> objects) {
        super(context,resource,objects);


        this.activity = context;
        this.myFriends = objects;
        this.winnerInterface = winnerInterface;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setTag(position); // This line is important.
        if(getItem(position).getProfile_image()!=null) {
            String imageurl = Base_url.concat(getItem(position).getProfile_image());
            Picasso.with(getContext()).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }
        holder.friendName.setText(getItem(position).getFirst_name());
        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
        } else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        return convertView;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    ((Request_for_winner)activity).updateData(getItem(position).getMain_challenge_id(),getItem(position).getAccpt_id(),getItem(position).getId());


                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }



    private static class ViewHolder {
        private TextView friendName;
        private CheckBox checkBox;
        private CircleImageView profile_image;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.check);
            friendName = (TextView) v.findViewById(R.id.name);
            profile_image = (CircleImageView) v.findViewById(R.id.profile_image);
        }
    }
}