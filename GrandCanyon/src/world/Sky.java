/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author Graeme
 */
public class Sky {
    
    private Spatial skyBox;
            
    private SimpleApplication sa;
 
    public Sky(SimpleApplication sa, String path, boolean isSphere){
        this.sa = sa;
        
        initSky(path, isSphere);
    }
    
    private void initSky(String path, boolean skySphere){
        Texture sky = sa.getAssetManager().loadTexture(path);
        
        skyBox = SkyFactory.createSky(sa.getAssetManager(), sky, skySphere);
        
        sa.getRootNode().attachChild(skyBox);
    }
}
