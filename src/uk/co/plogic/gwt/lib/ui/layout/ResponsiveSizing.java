package uk.co.plogic.gwt.lib.ui.layout;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A responsively sized widget is either an (i) absolute size (in pixels) or
 * (ii) in proportion to another element or widget.
 * 
 * For (ii) an absolute pixel adjustment can also be made (so the widget can
 * sit alongside padding & margins).
 *  
 * 
 * @author si
 *
 */
public class ResponsiveSizing {

	final Logger logger = Logger.getLogger("ResponsiveSizing");
	Widget scale_widget;
	Element scale_element;
	double heightScale = 1; // percent of parent panel's height this
	double widthScale = 1;  // should be.
	int widthAdjust = 0;   // pixel adjustments, can't be CSS as is responsive
	int heightAdjust = 0;
	int widthAbsolute = -1;
	int heightAbsolute = -1;
	
	private int width = -1;
	private int height = -1;

	
	public ResponsiveSizing(Widget w) {
		scale_widget = w;
	}
	
	public ResponsiveSizing(Element e) {
		scale_element = e;
	}
	
	public ResponsiveSizing(int width, int height) {
		// absolute pixel mode
		widthAbsolute = width;
		heightAbsolute = height;
	}
	
	/**
	 * 
	 * @param widthScale
	 * @param heightScale
	 */
	public void setScaleFactor(double widthScale, double heightScale) {
		this.heightScale = heightScale;
		this.widthScale = widthScale;
	}
	
	/**
	 * positive or negative to responsively adjust the size of carousels
	 * which are based on the size of another widget. @see setScaleFactor()
	 * @param heightAdjust
	 * @param widthAdjust
	 */
	public void setPixelAdjustments(int heightAdjust, int widthAdjust) {
		this.heightAdjust = heightAdjust;
		this.widthAdjust = widthAdjust;
	}
	
	/**
	 * some sizing info can be set in HTML5's 'data-' attributes.
	 * 
	 * "data-height", "data-width" can have values like-
	 * 
	 * XX% , percent of something (element or widget, not yet fully specified)
	 * 		 e.g. "20%"
	 * +XXpx adjustment. Added/removed from a relative scaling.
	 * -XXpx e.g. -27px
	 * 
	 * XXpx  absolute size in pixels
	 * 		 e.g. 300px
	 * 
	 * @param parentElement
	 */
	public void getElementAttributes(Element parentElement) {

		for( String att : new String [] {"data-height", "data-width"} ) {
			String domAttribute = parentElement.getAttribute(att);
			if(domAttribute != null && domAttribute.length() > 0) {
				if( domAttribute.endsWith("%")) {
					int clipTo = domAttribute.length()-1;
					String numb = domAttribute.substring(0, clipTo);
					Double pc = Double.parseDouble(numb) / 100;
					
					if(att.equals("data-height")) heightScale = pc;
					else						  widthScale = pc;

				} else if(domAttribute.endsWith("px")) {
					int clipTo = domAttribute.length()-2;
					String numb = domAttribute.substring(0, clipTo);
					int px = Integer.parseInt(numb);

					if(numb.startsWith("+") || numb.startsWith("-")) {
						if(att.equals("data-height")) heightAdjust = px;
						else						  widthAdjust = px;
					} else {
						if(att.equals("data-height")) heightAbsolute = px;
						else						  widthAbsolute = px;
					}

				} else {
					logger.warning("attribute ["+att+"] must end with '%' or 'px'");				
				}
			}
		}
	}

	private void updateDimensions() {

		double copiedWidth = -1;
		double copiedHeight = -1;
		if( scale_widget != null ) {
			copiedWidth = (double) scale_widget.getOffsetWidth();
			copiedHeight =  (double) scale_widget.getOffsetHeight();
		} else if( scale_element != null ) {
			copiedWidth = (double) scale_element.getOffsetWidth();
			copiedHeight =  (double) scale_element.getOffsetHeight();
		}
		
		if(copiedWidth == 0 || copiedHeight == 0) {
			logger.warning("Scaled item has a 0 sized dimension");
		}
		
		width = (int) (copiedWidth * widthScale);
		height = (int) (copiedHeight * heightScale);

		width += widthAdjust;
		height += heightAdjust;

		// one dimension could have been taken from a scale_element/widget, the other
		// could be absolute
		if( widthAbsolute > 0 )
			width = widthAbsolute;
		if( heightAbsolute > 0 )
			height = heightAbsolute;
	}

	
	public int getWidth() {
		updateDimensions();
		return width;
	}

	public int getHeight() {
		updateDimensions();
		return height;
	}

}
