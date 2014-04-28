package org.flowerplatform.flex_client.mindmap {
	import flash.system.Capabilities;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapConstants {
		
		//////////////////////////////////
		// Node types and categories
		//////////////////////////////////
		
		public static const MINDMAP_NODE_TYPE:String = "freeplaneNode"
		
		public static const FREEPLANE_MINDMAP_RESOURCE_KEY:String = "freePlaneMindMap";
		public static const FREEPLANE_PERSISTENCE_RESOURCE_KEY:String = "freePlanePersistence";
		
		public static const FREEPLANE_MINDMAP_CATEGORY:String = CoreConstants.CATEGORY_RESOURCE_PREFIX + FREEPLANE_MINDMAP_RESOURCE_KEY;
		public static const FREEPLANE_PERSISTENCE_CATEGORY:String = CoreConstants.CATEGORY_RESOURCE_PREFIX + FREEPLANE_PERSISTENCE_RESOURCE_KEY;
		
		//////////////////////////////////
		// Node properties
		//////////////////////////////////
		
		// CLOUD SHAPES
		public static const SHAPE_NONE:String = "none";		
		public static const SHAPE_RECTANGLE:String = "rectangle";
		public static const SHAPE_ROUND_RECTANGLE:String = "roundRectangle";
				
		public static const TEXT:String = "text";
		
		public static const MIN_WIDTH:String = "minWidth";
		public static const MAX_WIDTH:String = "maxWidth";
		public static const FONT_FAMILY:String = "fontFamily";
		public static const FONT_SIZE:String = "fontSize";
		public static const FONT_BOLD:String = "fontBold";
		public static const FONT_ITALIC:String = "fontItalic";
		
		public static const COLOR_TEXT:String = "colorText";
		public static const COLOR_BACKGROUND:String = "colorBackground";
		
		public static const CLOUD_COLOR:String = "cloudColor";
		public static const CLOUD_SHAPE:String = "cloudShape";
		
		public static const STYLE_NAME:String = "styleName";
		
		// EDGE STYLES
		public static const EDGE_SMOOTHLY_CURVED:String = "bezier";
		public static const EDGE_HIDE:String = "hide_edge";
		public static const EDGE_HORIZONTAL:String = "horizontal";
		public static const EDGE_LINEAR:String = "linear";
		// public static const EDGE_SHARP_LINEAR:String = "sharp_linear";
		// public static const EDGE_SHARPLY_CURVED:String = "sharp_bezier";
		
		public static const EDGE_WIDTH:String = "edgeWidth";
		public static const EDGE_STYLE:String = "edgeStyle";
		public static const EDGE_COLOR:String = "edgeColor";
		
		public static const ICONS_LIST:String =
			"help;yes;button_ok;button_cancel;bookmark;idea;messagebox_warning;stop-sign;closed;info;clanbomber;checked;unchecked;" 
			+ "wizard;gohome;knotify;password;pencil;xmag;bell;bookmark;launch;broken-line;stop;prepare;go;"
			+ "very_negative;negative;neutral;positive;very_positive;"
			+ "full-1;full-2;full-3;full-4;full-5;full-6;full-7;full-8;full-9;full-0;0%;25%;50%;75%;100%;"
			+ "attach;desktop_new;list;edit;kaddressbook;pencil;folder;kmail;Mail;revision;"
			+ "video;audio;executable;image;internet;internet_warning;mindmap;narrative;"
			+ "flag-black;flag-blue;flag-green;flag-orange;flag-pink;flag;flag-yellow;"
			+ "clock;clock2;hourglass;calendar;"
			+ "family;female1;female2;females;male1;male2;males;fema;group;"
			+ "ksmiletris;smiley-neutral;smiley-oh;smiley-angry;smily_bad;"
			+ "licq;penguin;freemind_butterfly;bee;"
			+ "forward;back;up;down;"
			+ "addition;subtraction;multiplication;division";

		
		/**
		 * Font size correction.
		 * When screenDPI = 72 DPI, the font size is a little bit too small to display, so use 92 DPI instead.
		 */ 
		public static const FONT_SCALE_FACTOR:Number = (Capabilities.screenDPI == 72 ? 96 : Capabilities.screenDPI) / 72;
		
		public static const MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE:String = "MindMapIconsWithButton";
		public static const MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE:String = "MindMapStyles";
		
		public static const NOTE:String = "note";
		
		//////////////////////////////////
		// Editor
		//////////////////////////////////
		
		public static const MINDMAP_CONTENT_TYPE:String = "mindmap";
		public static const MINDMAP_EDITOR_NAME:String = "MindMap Editor";
	}
}