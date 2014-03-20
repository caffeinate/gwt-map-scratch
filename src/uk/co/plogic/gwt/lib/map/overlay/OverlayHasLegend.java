package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;

public interface OverlayHasLegend {

	public String getOverlayId();
	public LegendAttributes getLegendAttributes();
	public void setLegendAttributes(LegendAttributes legendAttributes);
	public String getLegendTitle();
	public void setLegendTitle(String legendTitle);

}
