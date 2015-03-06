package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.map.markers.IconMarker;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.LatLng;

public class Points extends AbstractOverlay {

    ArrayList<IconMarker> markers = new ArrayList<IconMarker>();

    public Points(HandlerManager eventBus) {
        super(eventBus);
    }

    public void addPoint(LatLng position, String title) {
        IconMarker mapMarker = new IconMarker(eventBus, "", null, position, title);
        mapMarker.setMap(gMap);
        markers.add(mapMarker);
    }

    @Override
    public void clearAllMarkers() {
        for(IconMarker m : markers) {
            m.remove();
        }
        markers.clear();
    }

}
