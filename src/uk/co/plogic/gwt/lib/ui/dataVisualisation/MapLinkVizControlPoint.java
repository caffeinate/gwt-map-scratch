package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.widget.dataVisualisation.ChartVisualisation;
import uk.co.plogic.gwt.lib.widget.dataVisualisation.MapLinkedData;

public class MapLinkVizControlPoint {
    public String url;
    public String targetElementId;
    public String keyLabel;
    public String keyField;
    public String valueLabel;
    public String valueField;
    public ArrayList<String> valueFields;
    public String featureIdField;
    public String sortField;
    public String vAxisLabel;
    public ArrayList<MapLinkedData> result;
    public ChartVisualisation chart;

    public MapLinkVizControlPoint(String url, String targetElementId, String keyLabel,
            String keyField, String valueLabel, String valueField,
            ArrayList<String> vf, String featureIdField,
            String sortField, String vAxisLabel) {
        this.url = url;
        this.targetElementId = targetElementId;
        this.keyLabel = keyLabel;
        this.keyField = keyField;
        this.valueLabel = valueLabel;
        this.valueField = valueField;
        this.valueFields = vf;
        this.featureIdField = featureIdField;
        this.sortField = sortField;
        this.vAxisLabel = vAxisLabel;
    }
}
