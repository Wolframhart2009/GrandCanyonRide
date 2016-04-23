/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import mygame.CanyonMode;
import mygame.Main;
import world.World;

/**
 *
 * @author Robyn
 */
public class Raft {
    private CanyonMode msa;
    private Material matRaft;
    private Node nodeRaft;
    private Geometry[] geoms; // holds a geometry for each log in the raft
    private RigidBodyControl physRaft;
    private float xOffset, zOffset;
    
    private static final float OFFSETSPEED = 2f;
    private static final int NUM_LOGS_IN_RAFT = 5;
    
    public Raft(CanyonMode m, World w) {
        msa = m;
        xOffset = 0;
        zOffset = 0;
        
        initRaft(w);
        initKeys();
        initPhysics();
    }
    
    private void initRaft(World w) {
        Vector3f startPos = new Vector3f(0f, w.getWaterHeight() + 3f, 20f);
        geoms = new Geometry[NUM_LOGS_IN_RAFT];
        
        nodeRaft = new Node("Raft");
        nodeRaft.setLocalTranslation(startPos);
        
        matRaft = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRaft.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/log1.jpg"));
        
        Cylinder log = new Cylinder(32,32,1,10, true);
        
        for(int i = 0; i < NUM_LOGS_IN_RAFT; i++) {
            geoms[i] = new Geometry("log" + i, log);
            geoms[i].setMaterial(matRaft);
            geoms[i].setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            geoms[i].setLocalTranslation(-2.5f + (1.25f * i), 0f, 0f);
        }
        
        for(Geometry geom : geoms) {
            nodeRaft.attachChild(geom);
        }
        
        msa.getRootNode().attachChild(nodeRaft);
        
        RaftControl raftControl = new RaftControl();
        nodeRaft.addControl(raftControl);
        System.out.println("Raft added at location: " + nodeRaft.getLocalTranslation());
    }
    
    private void initKeys() {
        msa.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_I));
        msa.getInputManager().addMapping("Back", new KeyTrigger(KeyInput.KEY_K));
        msa.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        msa.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        msa.getInputManager().addMapping("Pos", new KeyTrigger(KeyInput.KEY_P));
        msa.getInputManager().addListener(analogListener, new String[]{"Forward", "Back", "Left", "Right", "Pos"});
    }
    
    private void initPhysics() {
        physRaft = new RigidBodyControl(2f);
        nodeRaft.addControl(physRaft);
        physRaft.setFriction(0f);
        msa.bullet.getPhysicsSpace().add(physRaft);
        physRaft.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        physRaft.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        physRaft.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
    }
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Left")) {
                xOffset += tpf * OFFSETSPEED;
            } else if (name.equals("Right")) {
                xOffset -= tpf * OFFSETSPEED;
            } else if (name.equals("Forward")) {
                zOffset += tpf * OFFSETSPEED;
            } else if (name.equals("Back")) {
                zOffset -= tpf * OFFSETSPEED;
            } else if (name.equals("Pos")){
                System.out.println(nodeRaft.getLocalTranslation());
            }
            
        }
    };
    
    class RaftControl extends AbstractControl {
        float speed = 10f;
        Vector3f impulseDir;
        
        @Override
        protected void controlUpdate(float tpf) {
//            Vector3f pos = nodeRaft.getWorldTranslation();
//            System.out.println("raft position: " + pos.x + ", " + pos.z);
            if(xOffset != 0 || zOffset != 0) {
                impulseDir = new Vector3f(xOffset, 0f, zOffset);
                impulseDir = impulseDir.mult(speed);
                physRaft.applyImpulse(impulseDir, Vector3f.ZERO);
                xOffset = 0;
                zOffset = 0;
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }    
}
