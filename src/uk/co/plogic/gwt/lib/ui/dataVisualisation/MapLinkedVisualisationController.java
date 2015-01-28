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
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

public class MapLinkedVisualisationController implements DropBox {

    protected Logger logger = Logger.getLogger("MapLinkedVisualisationController");
    protected ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

    class ControlPoint {
        String url;
        String targetElementId;
        ArrayList<String> targetFields;
        AttributeDictionary result;

        public ControlPoint(String url, String targetElementId,
                            ArrayList<String> targetFields) {
            this.url = url;
            this.targetElementId = targetElementId;
            this.targetFields = targetFields;
        }
    }

    public MapLinkedVisualisationController() {

    }

    public void add(String url, String targetElementId, ArrayList<String> fields_list) {

        ControlPoint c = new ControlPoint(url, targetElementId, fields_list);
        controlPoints.add(c);
    }

    @Override
    public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

        logger.info("got delivery for:"+letterBoxName);
        //logger.info(jsonEncodedPayload);

        // for now, the delivery can only be a list of dictionaries which are
        // converted into AttributeDictionarys
        JSONArray fullDoc = JSONParser.parseLenient(jsonEncodedPayload).isArray();


        // find all ControlPoints with this url
        for(ControlPoint c : controlPoints) {
            if( c.url.equals(letterBoxName)) {
                c.result = new AttributeDictionary();
                for(int i=0; i<fullDoc.size(); i++ ) {
                    JSONObject attribs = fullDoc.get(i).isObject();
                    for(String field: c.targetFields) {

                        if( attribs.containsKey(field) ) {
                            JSONValue value = attribs.get(field);
                            if( value.isNumber() != null )
                                c.result.set(field, value.isNumber().doubleValue());
                            else if(value.isString() != null)
                                c.result.set(field, value.isString().stringValue());
                            else
                                logger.warning("Unknown attribute data type field:"+field);
                        } else {
                            logger.warning("Failed to find requested field:"+field);
                        }
                    }
                }
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

}
