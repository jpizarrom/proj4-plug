package org.openstreetmap.josm.plugins.proj4;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.tools.GBC;

public class ProjectionPROJ4 implements org.openstreetmap.josm.data.projection.Projection, org.openstreetmap.josm.data.projection.ProjectionSubPrefs {

	private double dx = 0.0;
	private double dy = 0.0;
	public static int toCode = 0; 
	public static String[][] allCodes = new String[][] {
		{"epsg:32719","WGS 84 / UTM zone 19S"},
		{"epsg:32718", "WGS 84 / UTM zone 18S"},
		{"epsg:24878", "PSAD56 / UTM zone 18S"}
			};
	private static double[][] allBounds = new double[][] {
		{-72.0000, -80.0000, -66.0000, 0.0000 },
		{-78.0000, -80.0000, -72.0000, 0.0000 },
		{-78.0000, -49.0000, -72.0000, 0.0000 }
			};
//	private static String projCode = allCodes[toCode][0]; 
	
	private final com.jhlabs.map.proj.Projection projection;

	public ProjectionPROJ4() {
		super();
		projection = com.jhlabs.map.proj.ProjectionFactory.getNamedPROJ4CoordinateSystem(allCodes[toCode][0]);
	}
//	public ProjectionPROJ4(String proj) {
//		super();
//		projection = com.jhlabs.map.proj.ProjectionFactory.getNamedPROJ4CoordinateSystem(proj);
//	}
	public ProjectionPROJ4(int proj) {
		super();
		toCode = proj;
		projection = com.jhlabs.map.proj.ProjectionFactory.getNamedPROJ4CoordinateSystem(allCodes[toCode][0]);
		
	}
	
	@Override
	public EastNorth latlon2eastNorth(LatLon p) {
		Point2D.Double c = new Point2D.Double();
		c.x = p.lon();
		c.y = p.lat();
		//System.out.println("From " + c.x + " " + c.y);
		projection.transform( c, c );
		//System.out.println("To " + c.x + " " + c.y);
		return new EastNorth(c.x+dx, c.y+dy);
	}

	@Override
	public LatLon eastNorth2latlon(EastNorth p) {
		Point2D.Double c = new Point2D.Double();
		c.x = p.east()-dx;
		c.y = p.north()-dy;
		//System.out.println("InvFrom " + c.x + " " + c.y);
		projection.inverseTransform( c, c );
		//System.out.println("InvTo " + c.x + " " + c.y);
		return new LatLon(c.y, c.x);
	}

	@Override
	public Collection<String> getPreferences(JPanel arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getPreferencesFromCode(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPreferences(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setupPreferencePanel(JPanel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCacheDirectoryName() {
		// TODO Auto-generated method stub
		return allCodes[toCode][0].replace(":", "_");
	}

	@Override
	public double getDefaultZoomInPPD() {
		// TODO Auto-generated method stub
		return 0.009;
	}

	@Override
	public Bounds getWorldBoundsLatLon() {
		// TODO Auto-generated method stub
		return new Bounds(new LatLon(allBounds[toCode][1], allBounds[toCode][0]), new LatLon(allBounds[toCode][3], allBounds[toCode][2]));
	}

	@Override
	public String toCode() {
		// TODO Auto-generated method stub
		return allCodes[toCode][0];
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return tr(toCode() +" "+allCodes[toCode][1]);
	}

	public static String getProjCode() {
		return allCodes[toCode][0];
	}
//	public static String getProjCodeString() {
//		return tr(projCode +" "+allCodes[defaultCode][1]);
//	}
	public static String getProjCodeString(int i) {
		return tr(allCodes[i][0] +" "+allCodes[i][1]);
	}
}
