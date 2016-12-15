package ca.uoit.csci4100.assessments.finalexamstarter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * TODO:  Implement the missing methods from this class
 */

public class StoreArrayAdapter extends BaseAdapter {
    private Context context = null;
    private List<Store> storeLocations = null;

    public StoreArrayAdapter(Context context, List<Store> storeLocations) {
        this.context = context;
        this.storeLocations = storeLocations;
    }

    @Override
    public int getCount() {
        return storeLocations.size();
    }

    @Override
    public Object getItem(int position) {
        return storeLocations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * getView
     *
     * This function establishes a ViewGroup for a single item in a ListView.
     * The main function of this method is to put the data values into the
     * appropriate UI controls.
     *
     * @param position The index within the list of the current item
     * @param convertView An old ViewGroup to be reused, or null if none
     * @param parent The parent container (a ListView)
     *
     * @return The new ViewGroup, populated with all of the data elements
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate (R.layout.store_location_list_item, parent, false);
        }
        if (convertView != null){
            if (storeLocations.get(position).getId() == new PreferredStoreLocationDBHelper(context).getPreferredStore()){
                convertView.setBackgroundColor(Color.parseColor("#FFF0F0F0"));
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView distance = (TextView) convertView.findViewById(R.id.distance);

            String distanceStr = round(storeLocations.get(position).getDistance()) + context.getString(R.string.km);

            name.setText(storeLocations.get(position).getName());
            address.setText(storeLocations.get(position).getStreetAddress() + "\n" + storeLocations.get(position).getCity() + "\n" + storeLocations.get(position).getPostalCode());
            distance.setText(distanceStr);
        }

        return convertView;
    }

    /**
     * round
     *
     * This function rounds a double value to one decimal place (for better viewing of distance values)
     *
     * @param value The number to be rounded
     *
     * @return The number, rounded to 1 decimal place
     */
    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
