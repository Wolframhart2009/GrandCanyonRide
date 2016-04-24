/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obstacles;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import mygame.CanyonMode;
import mygame.Main;
import world.World;

/**
 *
 * @author Robyn
 */
public class FallingRock {
    private CanyonMode msa;
    private Node nodeRock;
    private Material matRock;
    private Geometry geomRock;
    private RigidBodyControl physRock;
    private Vector3f position;

    public FallingRock(CanyonMode m, World w, Vector3f position) {
        msa = m;
        this.position = position;
        
        initMaterial();
        initRock();
        initPhysics();
        placeRock(w);
    }
    
    private void initMaterial() {
        matRock = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRock.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/log1.jpg"));
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
        physRock = new RigidBodyControl(500f);
        geomRock.addControl(physRock);
        physRock.setRestitution(0.2f);
        msa.bullet.getPhysicsSpace().add(physRock);
        
        physRock.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_05);
        physRock.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01); // water, terrain
        physRock.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03); // raft
    }
    
    private void placeRock(World w) {
//        float x = -5.0f;
//        float y = w.getWaterHeight() + 5f;
//        float z = 5.0f;
        
        physRock.setPhysicsLocation(position);
    }
}
