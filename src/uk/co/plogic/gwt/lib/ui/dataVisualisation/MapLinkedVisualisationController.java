package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.jso.DataVisualisationDataJso;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.widget.dataVisualisation.ChartVisualisation;
import uk.co.plogic.gwt.lib.widget.dataVisualisation.MapLinkedData;

/**
 * A map visualisation (e.g. a chart) could be supplied with data from a
 * URL and then have an additional action when part of the map is selected
 * or moused over.
 *
 * Multiple visualisations could share a URL (i.e. they consume the same
 * data) but they must have separate target elements. The target Element is
 * their place in the DOM.
 *
 * @author si
 *
 */
public class MapLinkedVisualisationController implements DropBox {

    protected Logger logger = Logger.getLogger("MapLinkedVisualisationController");
    protected ArrayList<MapLinkVizControlPoint> controlPoints = new ArrayList<MapLinkVizControlPoint>();


    public MapLinkedVisualisationController() {

    }

    public MapLinkVizControlPoint add(String url, String targetElementId, String keyLabel,
                    String keyField, String valueLabel, String valueField,
                    JsArrayString valueFields, String featureIdField,
                    String sortField, String vAxisLabel) {

        // names of fields which values should be taken from.
        // i.e. multiple values in one record rather then multiple records where
        // the same field is used.
        ArrayList<String> vf = null;
        if( valueFields != null ) {
            vf = new ArrayList<String>();
            for(int i=0; i<valueFields.length(); i++) {
                vf.add(valueFields.get(i));
            }
        }

        MapLinkVizControlPoint c = new MapLinkVizControlPoint(url, targetElementId, keyLabel, keyField,
                                          valueLabel, valueField, vf, featureIdField,
                                          sortField, vAxisLabel);
        controlPoints.add(c);
        return c;
    }

    @Override
    public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

        logger.finer("got delivery for:"+letterBoxName);

        // for now, the delivery can only be a list of dictionaries which are
        // converted into AttributeDictionarys
        JSONArray fullDoc = JSONParser.parseLenient(jsonEncodedPayload).isArray();


        // find all ControlPoints with this url
        for(MapLinkVizControlPoint c : controlPoints) {
            if( c.url.equals(letterBoxName)) {
                c.result = new ArrayList<MapLinkedData>();
                for(int i=0; i<fullDoc.size(); i++ ) {
                    JSONObject attribs = fullDoc.get(i).isObject();

                    if( ! attribs.containsKey(c.keyField)
                     || ! attribs.containsKey(c.valueField) ) {
                        logger.warning("Can't find either value or key fields");
                    } else {
                        // for now, key must be a string and value must
                        // be a number because it's data for a graph
                        JSONValue jv = attribs.get(c.keyField);
                        String key = jv.isString().stringValue();
                        jv = attribs.get(c.valueField);
                        Double value = jv.isNumber().doubleValue();
                        jv = attribs.get(c.featureIdField);
                        String featureId = jv.isString().stringValue();

                        MapLinkedData ld = new MapLinkedData(key, value, featureId);
                        c.result.add(ld);
                    }
                }

                // TODO - sort only works on value, actually use the specified field
                if( c.sortField != null )
                    Collections.sort(c.result);

                if( c.chart != null ) {
                    c.chart.setChartData(c.keyField, c.valueField, c.result);
                    if( c.vAxisLabel != null )
                        c.chart.setVAxisLabel(c.vAxisLabel);
                }
            }
        }

    }

    /**
     * make HTTP requests
     */
    public void go() {

        HashMap<String, GeneralJsonService> uniqueUrls = new HashMap<String, GeneralJsonService>();
        for(MapLinkVizControlPoint c : controlPoints) {

            if( c.url == null )
                // charts can take their data from a map feature so there might
                // not be a url
                continue;

            if(! uniqueUrls.containsKey(c.url)) {
                GeneralJsonService gjson = new GeneralJsonService(c.url);
                gjson.setDeliveryPoint(this);
                uniqueUrls.put(c.url, gjson);
            }
        }

        for(String url : uniqueUrls.keySet()) {
            GeneralJsonService gjson = uniqueUrls.get(url);
            // use url as the letterbox name
            gjson.doRequest(url);
        }

    }

    public MapLinkVizControlPoint setChart(String id, ChartVisualisation chart) {

        for(MapLinkVizControlPoint c : controlPoints) {
            if( c.targetElementId.equals(id)) {
                c.chart = chart;

                if( c.result != null )
                    chart.setChartData(c.keyField, c.valueField, c.result);

                // assumes only one control point for a DOM id (targetElementId)
                return c;
            }
        }
        return null;
    }

}
