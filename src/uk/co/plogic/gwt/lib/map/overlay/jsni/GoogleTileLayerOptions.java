package uk.co.plogic.gwt.lib.map.overlay.jsni;

/*
 * Copyright 2011 The Google Web Toolkit Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.google.gwt.core.client.JavaScriptObject;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;

/**
 * This class is used to create a MapType that renders image tiles.
 *
 */
public class GoogleTileLayerOptions extends JavaScriptObject {

  /**
   * Callback type for handling asynchronous responses from getTileUrl.
   */
  public interface Callback  {

    /**
     * Called by the callback handler
     */ 
    String handle(Point a, double b);
  }

  public static final GoogleTileLayerOptions create(){
    return JavaScriptObject.createObject().cast();
  }


  /**
   * Protected constructor avoids default public constructor.
   */
  protected GoogleTileLayerOptions() {
    /* Java constructor is protected, */
  }

  /**
   * Alt text to markerDisplay when this MapType's button is hovered over in the
   * MapTypeControl.
   */ 
  public final native void setAlt(String alt)/*-{
    this.alt = alt;
  }-*/;

  /**
   * Returns a string (URL) for given tile coordinate (x, y) and zoom
   * level. This function should have a signature of:
   * <code>getTileUrl(<a href="#Point">Point</a>, number):string</code>
   */ 
  public final native void setGetTileUrl(Callback getTileUrl)/*-{
    var _getTileUrl = $entry(function(a, b) {
      return getTileUrl.@uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayerOptions.Callback::handle(Lcom/google/maps/gwt/client/Point;D)(a,b);

    }); 
    this.getTileUrl = _getTileUrl;
  }-*/;

  /**
   * The maximum zoom level for the map when displaying this MapType.
   */ 
  public final native void setMaxZoom(double maxZoom)/*-{
    this.maxZoom = maxZoom;
  }-*/;

  /**
   * The minimum zoom level for the map when displaying this MapType. Optional.
   */ 
  public final native void setMinZoom(double minZoom)/*-{
    this.minZoom = minZoom;
  }-*/;

  /**
   * Name to markerDisplay in the MapTypeControl.
   */ 
  public final native void setName(String name)/*-{
    this.name = name;
  }-*/;

  /**
   * The opacity to apply to the tiles.  The opacity should be specified
   * as a float value between 0 and 1.0, where 0 is fully transparent and
   * 1 is fully opaque.
   */ 
  public final native void setOpacity(double opacity)/*-{
    this.opacity = opacity;
  }-*/;

  /**
   * The tile size.
   */ 
  public final native void setTileSize(Size tileSize)/*-{
    this.tileSize = tileSize;
  }-*/;

}

