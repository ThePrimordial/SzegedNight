package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.SubscribedPage;
import fagagy.szeged.hu.szegednight.pubResources.PubBrowser;

/**
 * Created by rajat on 2/8/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] dataSource;
    private AdapterView.OnItemClickListener listener;

    public RecyclerAdapter(String[] dataArgs) {
        dataSource = dataArgs;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(dataSource[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View infoView = View.inflate(v.getContext(), R.layout.subscriber_view, null);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Ádám egy vámpír!")
                        .setView(infoView)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView =  (TextView) itemView.findViewById(R.id.list_item);

        }

    }
}
