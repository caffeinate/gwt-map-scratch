package uk.co.plogic.gwt.lib.map.overlay.jsni;

/* Copied and amended from...
 *
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
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.MVCObject;

/**
 * This class implements the MapType interface and is provided for
 * rendering image tiles.
 *
 */

public class GoogleTileLayer extends MVCObject {

  /**
   * This event is fired when the visible tiles have finished loading.
   */
  public interface TilesLoadedHandler  {

    /**
     * Override to handle event.
     */ 
    void handle();
  }

  /**
   * Constructs an ImageMapType using the provided ImageMapTypeOptions
   */ 
  public static native GoogleTileLayer create(GoogleTileLayerOptions opts)/*-{
    var _opts;
    if(@com.google.gwt.core.client.GWT::isScript()()) {
      _opts = opts;
    } else {
      _opts = {};
      for(k in opts) {
        if(k != "__gwt_ObjectId") {
          _opts[k] = opts[k];
        }
      }
    } 
    return new $wnd.google.maps.ImageMapType(_opts);
  }-*/;


  /**
   * Protected constructor avoids default public constructor.
   */
  protected GoogleTileLayer() {
    /* Java constructor is protected, */
  }

  /**
   * Adds the given listener function to the given event name for the
   * given object instance. Returns an identifier for this listener that
   * can be used with removeListener().
   */ 
  public final native void addTilesLoadedListener(TilesLoadedHandler handler)/*-{
    var _handler = $entry(function() {
      handler.@uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayer.TilesLoadedHandler::handle()();

    }); 
    $wnd.google.maps.event.addListener(this, "tilesloaded", _handler);
  }-*/;

  /**
   * Like addListener, but the handler removes itself after handling the first event.
   */ 
  public final native void addTilesLoadedListenerOnce(TilesLoadedHandler handler)/*-{
    var _handler = $entry(function() {
      handler.@uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayer.TilesLoadedHandler::handle()();

    }); 
    $wnd.google.maps.event.addListenerOnce(this, "tilesloaded", _handler);
  }-*/;

  /**
   * Removes all listeners for all events for the given instance.
   */ 
  public final native void clearInstanceListeners()/*-{
    $wnd.google.maps.event.clearInstanceListeners(this);
  }-*/;

  /**
   * Removes all listeners for the given event for the given instance.
   */ 
  public final native void clearTilesLoadedListeners()/*-{
    $wnd.google.maps.event.clearListeners(this, "tilesloaded");
  }-*/;

  /**
   * Returns the opacity level (
   * <code>0</code>
   * 
   *  (transparent) to 
   * <code>1.0</code>
   * 
   * ) of the 
   * <code>ImageMapType</code>
   * 
   * tiles.
   */ 
  public final native double getOpacity()/*-{
    return this.getOpacity();

  }-*/;

  /**
   * Sets the opacity level (
   * <code>0</code>
   * 
   *  (transparent) to 
   * <code>1.0</code>
   * 
   * ) of the 
   * <code>ImageMapType</code>
   * 
   * tiles.
   */ 
  public final native void setOpacity(double opacity)/*-{
    this.setOpacity(opacity);

  }-*/;

  /**
   * Triggers the given event. All arguments after eventName are passed as
   * arguments to the listeners.
   */ 
  public final native void triggerTilesLoaded(JavaScriptObject... varargs)/*-{
    var _varargs =
        @com.google.maps.gwt.client.ArrayHelper::toJsArray([Lcom/google/gwt/core/client/JavaScriptObject;)(varargs); 
    $wnd.google.maps.event.trigger(this, "tilesloaded", _varargs);
  }-*/;
  
  /**
   * Renders the ground overlay on the specified map. If map is set to
   * null, the overlay is removed.
 * @param t 
   */ 
  public final native void setMap(GoogleTileLayer t, GoogleMap map)/*-{
    map.overlayMapTypes.push(t);
  }-*/;

  /**
   * 
   */
  public final native void unsetMap(GoogleTileLayer t, GoogleMap map)/*-{
    
    remove_idx = -1;
    map.overlayMapTypes.forEach(function(item, index) {
    	if( item === t ) {
    		remove_idx = index;
    	}
	});
	if( remove_idx != -1 ) {
		map.overlayMapTypes.removeAt(remove_idx);
	}

  }-*/;
  
}

