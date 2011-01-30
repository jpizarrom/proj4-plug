package org.openstreetmap.josm.plugins.proj4;

import static org.openstreetmap.josm.tools.I18n.tr;
import static org.openstreetmap.josm.tools.I18n.marktr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.actions.UploadAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm.data.projection.Projection;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.preferences.ProjectionPreference;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class Proj4 extends Plugin {

    private JMenu mMenu;
    private Projection oldProj;
    private String projCode;
    private int projCodeTo = 0;

    public Proj4(PluginInformation info) {
        super(info);
        projCode = ProjectionPROJ4.getProjCode();
        refreshMenu();
    }

    public void toggleProjection()
    {
        Bounds b = (Main.map != null && Main.map.mapView != null) ? Main.map.mapView.getRealBounds() : null;

        try {
            // toggle projection
            if (Main.proj.toCode().equals(projCode)) {
                // use JOSM built in to fire Listeners
                ProjectionPreference.setProjection(oldProj.getClass().getName(), null);

                //Main.proj = oldProj;
                //UploadAction.unregisterUploadHook(uploadHook);
                //uploadHook = null;
            } else {
                oldProj = Main.proj;
                Main.proj = new ProjectionPROJ4(projCodeTo);
                //TODO use JOSM built in to fire Listeners, does not work currently due to classnotfound ex
                //     ProjectionPreference.setProjection(ProjectionEPSG31287.class.getName(), null);
                UploadAction.registerUploadHook(new PROJ4UploadHook(this));
            }
            // toggle Menu
            refreshMenu();
            // show info
            JOptionPane.showMessageDialog(
                    Main.parent,
                    tr("Current projection is set to {0}", Main.proj.toString()) +
                    (Main.proj.toCode().equals(projCode) ?
                            tr("\nPlease adjust WMS layer manually by using known exact objects/traces/... before starting to map")
                            : ""),
                            tr("Info"),
                            JOptionPane.INFORMATION_MESSAGE
            );
        } catch (final Exception e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(
                    Main.parent,
                    tr("The projection {0} could not be activated. Using Mercator", projCode),
                    tr("Error"),
                    JOptionPane.ERROR_MESSAGE
            );
            Main.proj = new Mercator();
        }
        if((b != null) && (oldProj != null) && (!Main.proj.getClass().getName().equals(oldProj.getClass().getName()) || Main.proj.hashCode() != oldProj.hashCode()))
        {
            Main.map.mapView.zoomTo(b);
            /* TODO - remove layers with fixed projection */
        }
    }

    public void refreshMenu() {
        MainMenu menu = Main.main.menu;

        if (mMenu == null)
            mMenu = menu.addMenu(marktr("PROJ4"), KeyEvent.VK_S, menu.defaultMenuPos, null);
        else
            mMenu.removeAll();
        // toggle menu text based on current projection
        for (int i=0; i< ProjectionPROJ4.allCodes.length; i++){
	        if (Main.proj.toCode().equalsIgnoreCase(ProjectionPROJ4.allCodes[i][0])) {
	        	addMenu("wmsmenu.png", projCode, oldProj.toString(), oldProj.toString(), i);
	        } else {
//	        	addMenu("wmsmenu_off.png", Main.proj.toString(), projCode, ProjectionPROJ4.getProjCodeString());
	        	addMenu("wmsmenu_off.png", Main.proj.toString(), ProjectionPROJ4.allCodes[i][0], ProjectionPROJ4.getProjCodeString(i), i);
	        }
        }
//        for (int i=0; i< ProjectionPROJ4.allCodes.length; i++)
//        	addMenu("wmsmenu_off.png", ProjectionPROJ4.allCodes[i][0], ProjectionPROJ4.allCodes[i][0], ProjectionPROJ4.getProjCodeString(i));
    }
    
    public void addMenu(String icon, String projFrom, String projTo, String projToString, final int i) {
    	JMenuItem m = new JMenuItem(new
                JosmAction(tr("set {0}", projToString)
                        ,icon
                        ,tr("set projection from {0} to {1}",projFrom ,projTo)
                        , null, false)
        {
    		public int proj = i;
//    		public void setProjCodeTo(){}
//            private static final long serialVersionUID = 7610502878925107646L;
            @Override
            public void actionPerformed(ActionEvent ev) {
            	projCodeTo = proj;
            	projCode = ProjectionPROJ4.allCodes[proj][0];
                toggleProjection();
            }
        });
        mMenu.add(m);
    }

}
