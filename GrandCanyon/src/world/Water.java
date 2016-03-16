/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author Graeme
 */
public class Water {
    
    private SimpleWaterProcessor watProcess;
    
    private SimpleApplication sa;
    
    public Water(SimpleApplication sa, World w){
    
        this.sa = sa;
        
        initWater(w);
    }
    
    private void initWater(World w){
        watProcess = new SimpleWaterProcessor(sa.getAssetManager());
        watProcess.setReflectionScene(sa.getRootNode());
        
        watProcess.setWaterDepth(40);         
        watProcess.setDistortionScale(0.05f); 
        watProcess.setWaveSpeed(0.05f);
        watProcess.setRenderSize(World.PATCH_SIZE, World.PATCH_SIZE);
        
        // we set the water plane
        Vector3f waterLocation=new Vector3f(0,World.NORMALIZE + 32,0);
        watProcess.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        sa.getViewPort().addProcessor(watProcess);

        Quad quad = new Quad(w.size, w.size);
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-200, -6, 250);
        water.setShadowMode(ShadowMode.Receive);
        water.setMaterial(watProcess.getMaterial());
        
        sa.getRootNode().attachChild(water);
    }
}
