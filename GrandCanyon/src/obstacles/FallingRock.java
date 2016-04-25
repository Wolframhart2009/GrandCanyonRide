/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obstacles;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import mygame.CanyonMode;
import mygame.CanyonRunMode;
import mygame.Main;
import world.World;

/**
 *
 * @author Robyn
 */
public class FallingRock {
    private CanyonMode msa;
    private CanyonRunMode saM;
    private Node nodeRock;
    private Material matRock;
    private Geometry geomRock;
    private RigidBodyControl physRock;
    private Vector3f position;
    private boolean runMode;
    private boolean hasHitWater;
    private AudioNode splash;
    
    public FallingRock(CanyonMode m, World w, Vector3f position) {
        msa = m;
        this.position = position;
        
        if(msa instanceof CanyonRunMode){
            runMode = true;
            saM = (CanyonRunMode) msa;
        }
        
        initMaterial();
        initRock();
        initPhysics();
        placeRock(w);
        initAudio();
    }
    
    private void initMaterial() {
        matRock = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRock.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/splat/sandstone.jpg"));
    }
    
    private void initRock() {
        nodeRock = new Node();
        
        Sphere rock = new Sphere(5, 7, 2.0f);
        geomRock = new Geometry("rock", rock);
        geomRock.setMaterial(matRock);
        geomRock.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        nodeRock.attachChild(geomRock);
        
        msa.getRootNode().attachChild(nodeRock);
    }
    
    private void initPhysics(){
        hasHitWater = false;
        
//        physRock = new RigidBodyControl(500f);
        CollisionShape rockShape = CollisionShapeFactory.createDynamicMeshShape(geomRock);
        physRock = new rockControl(rockShape, 10f);
        geomRock.addControl(physRock);
        physRock.setRestitution(0.2f);
        msa.bullet.getPhysicsSpace().add(physRock);
        
//        physRock.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_05);
//        physRock.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01); // water, terrain
//        physRock.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03); // raft
    }
    
    private void initAudio(){
        splash = new AudioNode(msa.getAssetManager(), "Sound/Rock/splash.wav", false);
        splash.setLooping(false);
        splash.setPositional(false);
        splash.setVolume(4.0f);
        msa.getRootNode().attachChild(splash); 
    }
    
    private void placeRock(World w) {
//        float x = -5.0f;
//        float y = w.getWaterHeight() + 5f;
//        float z = 5.0f;
        
        physRock.setPhysicsLocation(position);
    }
    
    public boolean getHasHitWater() {
        return hasHitWater;
    }
    
    public Vector3f getPosition() {
        return geomRock.getWorldTranslation();
    }
    
    public void remove() {
        msa.bullet.getPhysicsSpace().remove(physRock);
        geomRock.removeFromParent();
    }
    
    class rockControl extends RigidBodyControl implements PhysicsCollisionListener{ 

        public rockControl(CollisionShape extent, float mass){
            super(extent, mass);
            msa.bullet.getPhysicsSpace().addCollisionListener(this);
        }
        
        public void collision(PhysicsCollisionEvent event) {
            String objAName = event.getNodeA().getName();
            String objBName = event.getNodeB().getName();
//           System.out.println(objAName);
//           System.out.println(objBName);
   
            if(objAName.equals("rock") && objBName.equals("Raft")){
                if(runMode && !saM.getRecovery()){
                    saM.setRecovery();
                    saM.decHitPoints();
                }
            } else if(objAName.equals("Raft") && objBName.equals("rock")){
                if(runMode && !saM.getRecovery()){
                    saM.setRecovery();
                    saM.decHitPoints();
                }           
            } else if(objAName.equals("rock") && objBName.equals("Water_Collide_box")) {
//                System.out.println("rock water collision!");
                hasHitWater = true;
                splash.playInstance();
                RockSplashEmitter splash = new RockSplashEmitter(msa, msa.getRootNode(), getPosition());
            } else if(objAName.equals("Water_Collide_box") && objBName.equals("rock")) {
//                System.out.println("rock water collision!");
                hasHitWater = true;
                splash.playInstance();
                RockSplashEmitter splash = new RockSplashEmitter(msa, msa.getRootNode(), getPosition());
            }
        }
    }
}
