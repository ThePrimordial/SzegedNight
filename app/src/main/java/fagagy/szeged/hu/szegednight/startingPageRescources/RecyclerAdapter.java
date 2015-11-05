package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.SubscribedViewGenerator;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CardViewHolder> {

    private final List<String> identifiers;
    private Location location;
    private List<ParseObject> pubServerList = null;
    private List<ParseObject> subscribedServerList = null;
    private ParseFile fileObject;
    private SubscribedViewGenerator generator;
    private View infoView;
    private ImageView logo;
    private TextView description;
    private TextView actions;
    private TextView openingHours;
    private ImageButton facebook;
    private ImageButton navigate;

    public RecyclerAdapter(List<String> identifiers) {
        this.identifiers = identifiers;

        ParseQuery<ParseObject> pubQuery = ParseQuery.getQuery("Pub").fromLocalDatastore();
        try {
            pubServerList = pubQuery.fromPin("Pub").find();
        } catch (ParseException ignored) {
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subscribed").fromLocalDatastore();
        try {
            subscribedServerList = query.fromPin("Subscribed").find();
        } catch (ParseException ignored) {
        }

        }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item, parent, false);
        TextView name = (TextView) view.findViewById(R.id.cardview_name);
        ImageView logo = (ImageView) view.findViewById(R.id.cardview_background);

        LocationManager lm = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new CardViewHolder(view, name, logo);


    }

    //TODO holder 5.x implementation fail

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {

        generator = new SubscribedViewGenerator();
        fileObject = (ParseFile) subscribedServerList.get(position).get("Logo");
        Picasso.with(holder.itemView.getContext()).load(fileObject.getUrl()).into(holder.logo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                infoView = View.inflate(v.getContext(), R.layout.subscriber_view, null);
                logo = (ImageView) infoView.findViewById(R.id.subscriber_logo);
                description = (TextView) infoView.findViewById(R.id.subscriber_description);
                actions = (TextView) infoView.findViewById(R.id.subscriber_actions);
                openingHours = (TextView) infoView.findViewById(R.id.subscriber_openinghours);
                facebook = (ImageButton) infoView.findViewById(R.id.btnFacebook);
                navigate = (ImageButton) infoView.findViewById(R.id.btnNavigate);

                final int pubRowNumber = generator.getPubRowNumber(pubServerList, identifiers.get(position));
                final int subscriberRowNumber = generator.getSubscribedRowNumber(subscribedServerList, identifiers.get(position));

                fileObject = (ParseFile) subscribedServerList.get(position).get("Logo");
                Picasso.with(holder.itemView.getContext()).load(fileObject.getUrl()).into(logo);

                description.setText(generator.generateDescription(subscribedServerList, subscriberRowNumber));
                actions.setText(generator.generateOffers(subscribedServerList, subscriberRowNumber));
                openingHours.setText(generator.generateOpeningHours(pubServerList, pubRowNumber));
                generator.generateButtonActions(pubServerList, subscribedServerList, facebook, navigate,
                        pubRowNumber, subscriberRowNumber, location);

                new AlertDialog.Builder(v.getContext())
                        .setTitle(identifiers.get(position))
                        .setView(infoView)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return identifiers.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView logo;

        public CardViewHolder(View itemView, TextView name, ImageView logo) {
            super(itemView);
            this.name = name;
            this.logo = logo;
        }
    }
}
