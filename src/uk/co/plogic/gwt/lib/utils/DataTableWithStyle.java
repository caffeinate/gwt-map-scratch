package uk.co.plogic.gwt.lib.utils;

import com.googlecode.gwt.charts.client.DataTable;

public class DataTableWithStyle extends DataTable {

	protected DataTableWithStyle(){}
	
	public static native DataTableWithStyle create() /*-{
		return new $wnd.google.visualization.DataTable();
	}-*/;
	
	public final native int addStyleColumn()/*-{
    	return this.addColumn({type:'string',role:'style'});
    }-*/;
}
