package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.widget.dataVisualisation.ChartVisualisation;
import uk.co.plogic.gwt.lib.widget.dataVisualisation.MapLinkedData;

public class MapLinkedVisualisationController implements DropBox {

    protected Logger logger = Logger.getLogger("MapLinkedVisualisationController");
    protected ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

    class ControlPoint {
        String url;
        String targetElementId;
        String keyField;
        String valueField;
        String featureIdField;
        ArrayList<MapLinkedData> result;
        ChartVisualisation chart;

        public ControlPoint(String url, String targetElementId, String keyField,
                            String valueField, String featureIdField) {
            this.url = url;
            this.targetElementId = targetElementId;
            this.keyField = keyField;
            this.valueField = valueField;
            this.featureIdField = featureIdField;
        }
    }

    public MapLinkedVisualisationController() {

    }

    public void add(String url, String targetElementId, String keyField,
                    String valueField, String featureIdField) {

        ControlPoint c = new ControlPoint(url, targetElementId, keyField,
                                          valueField, featureIdField);
        controlPoints.add(c);
    }

    @Override
    public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

        logger.finer("got delivery for:"+letterBoxName);

        // for now, the delivery can only be a list of dictionaries which are
        // converted into AttributeDictionarys
        JSONArray fullDoc = JSONParser.parseLenient(jsonEncodedPayload).isArray();


        // find all ControlPoints with this url
        for(ControlPoint c : controlPoints) {
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

                if( c.chart != null )
                    c.chart.setChartData(c.keyField, c.valueField, c.result);
            }
        }

    }

    /**
     * make HTTP requests
     */
    public void go() {

        HashMap<String, GeneralJsonService> uniqueUrls = new HashMap<String, GeneralJsonService>();
        for(ControlPoint c : controlPoints) {
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

    public void setChart(String id, ChartVisualisation chart) {

        for(ControlPoint c : controlPoints) {
            if( c.targetElementId.equals(id)) {
                c.chart = chart;

                if( c.result != null )
                    chart.setChartData(c.keyField, c.valueField, c.result);
            }
        }
    }

}
