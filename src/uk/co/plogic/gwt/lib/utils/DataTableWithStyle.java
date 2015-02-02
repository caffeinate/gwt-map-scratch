package uk.co.plogic.gwt.lib.utils;

import com.googlecode.gwt.charts.client.DataTable;

public class DataTableWithStyle extends DataTable {

	public static native DataTableWithStyle create() /*-{
	return new $wnd.google.visualization.DataTable();
}-*/;
	
	public final native int addCertaintyColumn()/*-{
    return this.addColumn({type:'boolean',role:'certainty'});
    }-*/;
}
