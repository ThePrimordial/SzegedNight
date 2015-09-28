package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.SubscribedViewGenerator;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<String> identifiers;
    private AdapterView.OnItemClickListener listener;
    private Location location;
    private List<ParseObject> pubServerList = null;
    private List<ParseObject> subscribedServerList = null;

    public RecyclerAdapter(List<String> identifiers) {
        this.identifiers = identifiers;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item, parent, false);

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

        LocationManager lm = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        return new ViewHolder(view);


    }

    //TODO holder 5.x implementation fail

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SubscribedViewGenerator generator = new SubscribedViewGenerator();
        final int subscriberRowNumber = generator.getSubscribedRowNumber(subscribedServerList, identifiers.get(position));
        //holder.name.setText(subscribedServerList.get(position).getString("Name"));
        ParseFile fileObject = (ParseFile) subscribedServerList.get(subscriberRowNumber).get("Logo");
        fileObject.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    //holder.logo.setImageBitmap(bmp);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View infoView = View.inflate(v.getContext(), R.layout.subscriber_view, null);
                final ImageView logo = (ImageView) infoView.findViewById(R.id.subscriber_logo);
                TextView description = (TextView) infoView.findViewById(R.id.subscriber_description);
                TextView actions = (TextView) infoView.findViewById(R.id.subscriber_actions);
                TextView openingHours = (TextView) infoView.findViewById(R.id.subscriber_openinghours);
                ImageButton facebook = (ImageButton) infoView.findViewById(R.id.btnFacebook);
                ImageButton navigate = (ImageButton) infoView.findViewById(R.id.btnNavigate);

                final int pubRowNumber = generator.getPubRowNumber(pubServerList, identifiers.get(position));
                final int subscriberRowNumber = generator.getSubscribedRowNumber(subscribedServerList, identifiers.get(position));

                ParseFile fileObject = (ParseFile) subscribedServerList.get(subscriberRowNumber).get("Logo");
                fileObject.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            logo.setImageBitmap(bmp);
                        }
                    }
                });


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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView logo;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = (TextView) itemView.findViewById(R.id.cardview_name);
            logo = (ImageView) itemView.findViewById(R.id.cardview_background);

        }

    }
}
