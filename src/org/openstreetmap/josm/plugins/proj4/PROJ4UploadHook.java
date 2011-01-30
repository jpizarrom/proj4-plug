package org.openstreetmap.josm.plugins.proj4;


import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.upload.UploadHook;
import org.openstreetmap.josm.data.APIDataSet;

public class PROJ4UploadHook implements UploadHook {

    private final Proj4 plugin;

    public PROJ4UploadHook(Proj4 plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean checkUpload(APIDataSet arg0) {
//        if (Main.proj.toCode().equals(ProjectionPROJ4.getProjCode())) {
//            plugin.toggleProjection();
//        }
        return true;
    }

}
