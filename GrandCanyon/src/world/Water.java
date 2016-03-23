/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;
import mygame.Main;

/**
 *
 * @author Graeme
 */
public class Water {
    
    private SimpleWaterProcessor watProcess;
    public Spatial waterNode;
    
    private Main msa;
    
    public Water(Main m, World w){
    
        msa = m;
        
        initWater(w);
        initPhysics();
    }
    
    private void initWater(World w){
        watProcess = new SimpleWaterProcessor(msa.getAssetManager());
        watProcess.setReflectionScene(msa.getRootNode());
        watProcess.setRenderSize(World.PATCH_SIZE, World.PATCH_SIZE);
        
        msa.getViewPort().addProcessor(watProcess);
        
        watProcess.setWaterDepth(5);         
        watProcess.setDistortionScale(0.02f); 
        watProcess.setWaveSpeed(0.05f);

        waterNode =(Spatial)  msa.getAssetManager().loadModel("Models/WaterTest/WaterTest.mesh.xml");
        waterNode.setMaterial(watProcess.getMaterial());
        waterNode.setLocalScale(4096);
        waterNode.setLocalTranslation(0, -(World.NORMALIZE)  + 150, 0);
        
        System.out.println("Water added!");
        
        msa.getRootNode().attachChild(waterNode);
    }
    
    private void initPhysics() {
        RigidBodyControl waterPhys = new RigidBodyControl(0f);
        waterNode.addControl(waterPhys);
        msa.bullet.getPhysicsSpace().add(waterPhys);
    }
}
