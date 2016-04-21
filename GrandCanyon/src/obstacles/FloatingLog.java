/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obstacles;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import mygame.CanyonMode;
import mygame.Main;
import world.World;

/**
 *
 * @author Robyn
 */
public class FloatingLog {
    private CanyonMode msa;
    private Node nodeLog;
    private Material matLog;
    private Geometry geomLog;
    private RigidBodyControl physLog;
    
    public FloatingLog(CanyonMode m, World w) {
        msa = m;
        
        initMaterial();
        initLog();
        initPhysics();
        placeLog(w);
    }
    
    private void initMaterial() {
        matLog = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matLog.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/log1.jpg"));
    }
    
    private void initLog() {
        nodeLog = new Node();
        
        Cylinder log = new Cylinder(32,32,1,10, true);
        geomLog = new Geometry("log", log);
        geomLog.setMaterial(matLog);
        geomLog.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        nodeLog.attachChild(geomLog);
        
        msa.getRootNode().attachChild(nodeLog);
    }
    
    private void initPhysics() {
        physLog = new RigidBodyControl(5f);
        geomLog.addControl(physLog);
        physLog.setRestitution(0.2f);
        msa.bullet.getPhysicsSpace().add(physLog);        
    }
    
    private void placeLog(World w) {
        float x = -5.0f;
        float y = w.getWaterHeight() + 5f;
        float z = 5.0f;
        
        physLog.setPhysicsLocation(new Vector3f(x, y, z));
    }    
}
