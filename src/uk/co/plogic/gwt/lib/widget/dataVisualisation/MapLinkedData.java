package uk.co.plogic.gwt.lib.widget.dataVisualisation;

public class MapLinkedData {
    String key;
    Double value;
    String featureId;
    int rowId;
    public MapLinkedData(String key, Double value, String featureId) {
        this.key = key;
        this.value = value;
        this.featureId = featureId;
    }
}
