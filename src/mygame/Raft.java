/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Robyn
 */
public class Raft {
    Main msa;
    Material matRaft;
    Geometry geomRaft;
    RigidBodyControl physRaft;
    
    public Raft(Main m) {
        msa = m;
        
        initRaft();
        initPhysics();
    }
    
    private void initRaft() {
        matRaft = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRaft.setBoolean("UseMaterialColors", true);
        matRaft.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        matRaft.setColor("Diffuse", new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        matRaft.setColor("Specular", ColorRGBA.White);
        matRaft.setFloat("Shininess", 10f); // shininess from 1-128
//        matRaft.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/textureMarble.png"));
        
        Sphere s = new Sphere(32, 32, 5f);
        geomRaft = new Geometry("raft", s);
        geomRaft.setMaterial(matRaft);
        geomRaft.setLocalTranslation(new Vector3f(0f, 0f, 0f));
        geomRaft.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        msa.getRootNode().attachChild(geomRaft);
    }
    
    private void initPhysics() {
        physRaft = new RigidBodyControl(2f);
        geomRaft.addControl(physRaft);
        msa.bullet.getPhysicsSpace().add(physRaft);
    }
}
