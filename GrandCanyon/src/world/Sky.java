/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import mygame.CanyonMode;

/**
 *
 * @author Graeme
 */
public class Sky {
    
    private Spatial skyBox;
            
    private CanyonMode sa;
    private DirectionalLight light;
 
    public Sky(CanyonMode sa, String path, boolean isSphere, DirectionalLight li){
        this.sa = sa;
        this.light = li;
        
        initSky(path, isSphere);
        initFilters();
    }
    
    private void initSky(String path, boolean skySphere){        
        skyBox = SkyFactory.createSky(sa.getAssetManager(), path, skySphere);
        
        sa.getRootNode().attachChild(skyBox);
    }
    
    private void initFilters(){
         FilterPostProcessor fpp = new FilterPostProcessor(sa.getAssetManager());
         
         BloomFilter bloom = new BloomFilter();
         bloom.setExposurePower(55);
         bloom.setBloomIntensity(1.0f);
         fpp.addFilter(bloom);
        
         LightScatteringFilter lsf = new LightScatteringFilter(light.getDirection().mult(-300));
         lsf.setLightDensity(1.0f);
         fpp.addFilter(lsf);
        
         DepthOfFieldFilter dof = new DepthOfFieldFilter();
         dof.setFocusDistance(0);
         dof.setFocusRange(100);
         fpp.addFilter(dof);
         
         sa.getViewPort().addProcessor(fpp);
    }
}
