/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
import world.World;

/**
 *
 * @author Robyn
 */
public class Raft {
    private Main msa;
    private Material matRaft;
    private Node nodeRaft;
    private Geometry geomRaft;
    private RigidBodyControl physRaft;
    float xOffset, zOffset;
    
    private static final float OFFSETSPEED = 2f;
    
    public Raft(Main m, World w) {
        msa = m;
        xOffset = 0;
        zOffset = 0;
        
        initRaft(w);
        initKeys();
        initPhysics();
    }
    
    private void initRaft(World w) {
        nodeRaft = new Node();
        
        matRaft = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRaft.setBoolean("UseMaterialColors", true);
        matRaft.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        matRaft.setColor("Diffuse", new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        matRaft.setColor("Specular", ColorRGBA.White);
        matRaft.setFloat("Shininess", 10f);
        
        Box b = new Box(5,1,5);
        geomRaft = new Geometry("raft", b);
        geomRaft.setMaterial(matRaft);
        geomRaft.setLocalTranslation(new Vector3f(0f, w.getWaterHeight() + 3f, 20f));
        geomRaft.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        nodeRaft.attachChild(geomRaft);
        
        msa.getRootNode().attachChild(nodeRaft);
        
        RaftControl raftControl = new RaftControl();
        geomRaft.addControl(raftControl);
        System.out.println("Raft added at location: " + geomRaft.getLocalTranslation());
    }
    
    private void initKeys() {
        msa.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_I));
        msa.getInputManager().addMapping("Back", new KeyTrigger(KeyInput.KEY_K));
        msa.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        msa.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        msa.getInputManager().addListener(analogListener, new String[]{"Forward", "Back", "Left", "Right"});
    }
    
    private void initPhysics() {
        physRaft = new RigidBodyControl(2f);
        geomRaft.addControl(physRaft);
        msa.bullet.getPhysicsSpace().add(physRaft);
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
            }
        }
    };
    
    class RaftControl extends AbstractControl {
        float speed = 10f;
        Vector3f impulseDir;
        
        @Override
        protected void controlUpdate(float tpf) {
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
