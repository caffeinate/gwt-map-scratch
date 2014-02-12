package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;

public class Slider extends SliderBarHorizontal {
        
        ImagesKDEHorizontalLeftBW images = GWT.create(ImagesKDEHorizontalLeftBW.class);
        
        public Slider(int maxValue, String width) {
            setLessWidget(new Image(images.less()));
            //setMoreWidget(new Image(images.more()));
            setScaleWidget(new Image(images.scale().getUrl()), 16);
            setMoreWidget(new Image(images.more()));
            setDragWidget(new Image(images.drag()));
            this.setWidth(width);
            this.setMaxValue(maxValue);         
        }
                
        interface ImagesKDEHorizontalLeftBW extends ClientBundle {
                
                @Source("sliderImages/draggmap.png")
                ImageResource drag();

                @Source("sliderImages/lessgmap.png")
                ImageResource less();

                @Source("sliderImages/moregmap.png")
                ImageResource more();

                @Source("sliderImages/scalegmap.png")
                DataResource scale();
        }       
        
}