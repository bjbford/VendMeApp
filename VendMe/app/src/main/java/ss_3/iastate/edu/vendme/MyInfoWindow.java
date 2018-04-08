package ss_3.iastate.edu.vendme;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Custom info window that is displayed when a map marker is clicked.
 *  - Created by bjbford on 1/29/18.
 *  - Integrated into main application by jdanner.
 */

public class MyInfoWindow implements GoogleMap.InfoWindowAdapter{
        //GoogleMap.OnInfoWindowClickListener{

    private Activity myActivity;

    public MyInfoWindow(Activity myActivity){
        this.myActivity = myActivity;
    }

    /**
     * Custom implementation of a Markers info window because default doesn't show
     * enough snippet information.
     * @param marker
     * @return custom View
     */
    @Override
    public View getInfoContents(Marker marker){
        View myView = myActivity.getLayoutInflater().inflate(R.layout.custom_marker_info_window,null);
        TextView myTitle = myView.findViewById(R.id.myTitle);
        TextView mySnippet = myView.findViewById(R.id.mySnippet);
        myTitle.setText(marker.getTitle());
        mySnippet.setText(marker.getSnippet());
        return myView;
    }

    /**
     * Not used, but had to be implemented to avoid error when using GoogleMap.InfoWindowAdapter
     * to create a custom info window.
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

//    @Override
//    public void onInfoWindowClick(Marker marker){
//        Toast.makeText("Bring up full details when Info Window Clicked",this,Toast.LENGTH_SHORT).show();
//    }
}
