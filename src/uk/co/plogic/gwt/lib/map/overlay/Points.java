package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;

import uk.co.plogic.gwt.lib.events.MapViewChangedEvent;
import uk.co.plogic.gwt.lib.events.MapViewChangedEventHandler;
import uk.co.plogic.gwt.lib.map.MapUtils;
import uk.co.plogic.gwt.lib.map.markers.IconMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;
import uk.co.plogic.gwt.lib.map.overlay.resources.OverlayImageResource;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Point;

public class Points extends AbstractOverlay {

    //ArrayList<IconMarker> markers = new ArrayList<IconMarker>();
    protected HashMap<String, IconMarker> markers =
            new HashMap<String, IconMarker>();
    protected FlowPanel info_marker;
    protected String markerTemplate;
    OverlayImageResource images;

    public Points(HandlerManager eventBus) {
        super(eventBus);
        images = GWT.create(OverlayImageResource.class);

        eventBus.addHandler(MapViewChangedEvent.TYPE, new MapViewChangedEventHandler() {

            @Override
            public void onMapViewChangedEvent(MapViewChangedEvent event) {
                if( info_marker != null )
                    info_marker.setVisible(false);
            }
        });

    }

    public void addPoint(String id, LatLng position, String title) {
        IconMarker mapMarker = new IconMarker(eventBus, id, null, position, title);
        mapMarker.setMap(gMap);
        mapMarker.setOverlay(this);

        if( markers.containsKey(id) )
            markers.get(id).remove();

        markers.put(id, mapMarker);
    }

    @Override
    public void clearAllMarkers() {

        for(IconMarker m : markers.values()) {
            m.remove();
        }
        markers.clear();
    }

    @Override
    public void userInteractionWithMarker(UserInteraction interactionType, String markerId, LatLng latLng) {

        logger.finer("userInteraction for markerId:"+markerId+" in layer:"+getOverlayId());

        if( ! markers.containsKey(markerId) )
            return;

        IconMarker targetMarker = markers.get(markerId);

        // lock marker as selected
        if( interactionType == UserInteraction.CLICK ) {
            annotateMarker(targetMarker, latLng);
        }

    }

    /**
     * show an info window.
     *
     * There is a lot of duplication between this and Shapes.annotateMarker.
     * But putting it somewhere shared proved hard because of focusOnMarker
     * and lockedFocusMarker.
     *
     * @param targetMarker
     * @param latLng
     */
    protected void annotateMarker(IconMarker targetMarker, LatLng latLng) {

        if( info_marker == null ) {
            final String mname = "marker_info_box";
            info_marker = mapAdapter.createMapOverlayPanel(mname, mname);
        }

        if( targetMarker == null ||  latLng == null || markerTemplate == null ) {
            info_marker.setVisible(false);
            return;
        }

        AttributeDictionary markerData = new AttributeDictionary();
        markerData.set("title", targetMarker.getTitle());
        int titleLen = targetMarker.getTitle().length()+1;

        String builtHtml = StringUtils.renderHtml(markerTemplate, markerData);
        Point p = MapUtils.LatLngToPixel(gMap, latLng);
        HTML h = new HTML(builtHtml);
        h.setStyleName("marker_info_box_simple_copy");

        info_marker.clear();

        Image closeButton = new Image(images.close());
        closeButton.setStyleName("marker_info_box_close");
        closeButton.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                info_marker.setVisible(false);
                clearAllMarkers();
                hide();
            }
        });
        info_marker.add(closeButton);

        info_marker.add(h);
        double offsetX = p.getX();
        double offsetY = p.getY()-10;

        info_marker.getElement().setAttribute(
                                        "style",
                                        "left: "+offsetX+"px;top: "+offsetY+"px;width:"+titleLen+"em;"
                                             );
        info_marker.setVisible(true);

    }

    public void setInfoMarkerTemplate(String template) {
        markerTemplate = template;
    }
}
