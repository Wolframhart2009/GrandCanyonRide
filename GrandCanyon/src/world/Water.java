/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import mygame.Main;

/**
 *
 * @author Graeme
 */
public class Water {
    
    public Spatial waterNode;
    private WaterFilter watFilter;
    
    private Main msa;
    private DirectionalLight light;
    
    public Water(Main m, World w, DirectionalLight l){
    
        this.msa = m;
        this.light = l;
        
        initWater(w);
        addWaterCollision(w);
        initPhysics();
    }
    
    private void initWater(World w){
         watFilter = new WaterFilter(msa.getRootNode(), light.getDirection());
         FilterPostProcessor fpp = new FilterPostProcessor(msa.getAssetManager());

         fpp.addFilter(watFilter);
         
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
         
         watFilter.setWaveScale(0.003f);
         watFilter.setMaxAmplitude(2f);
         watFilter.setFoamExistence(new Vector3f(1f, 4, 0.5f));
         watFilter.setFoamTexture((Texture2D) msa.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));

         watFilter.setRefractionStrength(0.2f);

         watFilter.setWaterHeight(w.getWaterHeight());
         
         msa.getViewPort().addProcessor(fpp);
    }
    
    private void addWaterCollision(World w){
       Box WaterCollideMesh = new Box(w.size, 1.0f, w.size);
       waterNode = new Geometry("Water_Collide_box", WaterCollideMesh);
       waterNode.setMaterial(new Material(msa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
       
       msa.getRootNode().attachChild(waterNode);
       waterNode.setLocalTranslation(0, w.getWaterHeight(), 0);
       waterNode.setCullHint(Spatial.CullHint.Always); //This makes it invisible
    }
    
    private void initPhysics() {
        RigidBodyControl waterPhys = new RigidBodyControl(0f);
        waterNode.addControl(waterPhys);
        msa.bullet.getPhysicsSpace().add(waterPhys);
    }
}
