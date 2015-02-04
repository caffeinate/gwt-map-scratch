package uk.co.plogic.gwt.lib.widget.dataVisualisation;

public class MapLinkedData implements Comparable<MapLinkedData> {
    String key;
    Double value;
    String featureId;
    int rowId;
    public MapLinkedData(String key, Double value, String featureId) {
        this.key = key;
        this.value = value;
        this.featureId = featureId;
    }
    @Override
    public int compareTo(MapLinkedData o) {
        //ascending order
        Double d = (value - o.value)*1000;
        return d.intValue();
    }
}
