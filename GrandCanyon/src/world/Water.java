/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import mygame.CanyonMode;
import mygame.Main;

/**
 *
 * @author Graeme
 */
public class Water {
    
    public Spatial waterNode;
    private AudioNode waterAudio;
    private WaterFilter watFilter;
    private RigidBodyControl waterPhys;
    
    private CanyonMode msa;
    private DirectionalLight light;
    private World world;
    
    public Water(CanyonMode m, World w, DirectionalLight l){
    
        this.msa = m;
        this.light = l;
        this.world = w;
        
        initWater();
        addWaterCollision(w);
        initPhysics();
        waterNode.addControl(new WaterControl());
        initAudio();
    }
    
    private void initWater(){
         watFilter = new WaterFilter(msa.getRootNode(), light.getDirection());
         FilterPostProcessor fpp = new FilterPostProcessor(msa.getAssetManager());

         fpp.addFilter(watFilter);
         
         watFilter.setWaveScale(0.003f);
         watFilter.setMaxAmplitude(2f);
         watFilter.setFoamExistence(new Vector3f(1f, 4, 0.5f));
         watFilter.setFoamTexture((Texture2D) msa.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));

         watFilter.setRefractionStrength(0.2f);

         watFilter.setWaterHeight(world.getWaterHeight());
         
         msa.getViewPort().addProcessor(fpp);
    }
    
    private void addWaterCollision(World w){
        Box WaterCollideMesh = new Box(w.size, .30f, w.size);
        waterNode = new Geometry("Water_Collide_box", WaterCollideMesh);
        waterNode.setMaterial(new Material(msa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
       
        msa.getRootNode().attachChild(waterNode);
        waterNode.setLocalTranslation(0, w.getWaterHeight(), 0);
        waterNode.setCullHint(Spatial.CullHint.Always); //This makes it invisible
        
    }
    
    private void initPhysics() {
        BoxCollisionShape waterShape = (BoxCollisionShape) CollisionShapeFactory.createBoxShape(waterNode);
        waterPhys = new RigidBodyControl(waterShape, 0f);
        waterPhys.setRestitution(0.0f);
        waterNode.addControl(waterPhys);
        msa.bullet.getPhysicsSpace().add(waterPhys);
//        waterPhys.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
//        waterPhys.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
//        waterPhys.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_05);
    }
    
    private void initAudio(){
        waterAudio = new AudioNode(msa.getAssetManager(), "Sound/Water/Water_Lapping_Boat.wav");
        waterAudio.setLooping(true);
        waterAudio.setPositional(false);
        waterAudio.setVolume(.90f);
        msa.getRootNode().attachChild(waterAudio);
        waterAudio.playInstance();
    }
    
    public void updateWaterHeight(){
//        System.out.print("Old height: " + waterPhys.getPhysicsLocation() + " || new Height: " + waterPhys.getPhysicsLocation().addLocal(
//                0,
//                (world.getWaterHeight() - waterPhys.getPhysicsLocation().y),
//                0) + "\n");
        
        watFilter.setWaterHeight(world.getWaterHeight());
        waterPhys.setPhysicsLocation(waterPhys.getPhysicsLocation().addLocal(
                0,
                (world.getWaterHeight() - waterPhys.getPhysicsLocation().y),
                0));
    }
    
    public void stopAudio() {
        waterAudio.stop();
    }
    
    class WaterControl extends AbstractControl{
        
        private float time;
        
        public WaterControl(){
            
        }

        @Override
        protected void controlUpdate(float tpf) {
              //Generates minor waves
            
              time += tpf;
              float waveHeight = (float) Math.cos((time) % FastMath.TWO_PI)/4;
              watFilter.setWaterHeight(world.getWaterHeight() + waveHeight);
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            
        }
    }
}
