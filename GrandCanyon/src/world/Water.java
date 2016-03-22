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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author Graeme
 */
public class Water {
    
    private SimpleWaterProcessor watProcess;
    public Spatial waterNode;
    
    private SimpleApplication sa;
    
    public Water(SimpleApplication sa, World w){
    
        this.sa = sa;
        
        initWater(w);
    }
    
    private void initWater(World w){
        watProcess = new SimpleWaterProcessor(sa.getAssetManager());
        watProcess.setReflectionScene(sa.getRootNode());
        watProcess.setRenderSize(World.PATCH_SIZE, World.PATCH_SIZE);
        
        sa.getViewPort().addProcessor(watProcess);
        
        watProcess.setWaterDepth(5);         
        watProcess.setDistortionScale(0.02f); 
        watProcess.setWaveSpeed(0.05f);

        waterNode =(Spatial)  sa.getAssetManager().loadModel("Models/WaterTest/WaterTest.mesh.xml");
        waterNode.setMaterial(watProcess.getMaterial());
        waterNode.setLocalScale(4096);
        waterNode.setLocalTranslation(0, -(World.NORMALIZE)  + 150, 0);
        
        System.out.println("Water added!");
        
        sa.getRootNode().attachChild(waterNode);
    }
}
