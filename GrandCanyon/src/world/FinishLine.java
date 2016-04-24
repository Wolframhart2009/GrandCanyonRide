/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.CanyonMode;
import mygame.CanyonRunMode;

/**
 *
 * @author Robyn
 */
public class FinishLine extends CourseLine {
    private CanyonMode msa;
    private Vector3f position;
    private Material mat;
    
    private Node nodeFinish;
    private Geometry geomFinishBox;
    private GhostControl physFinish;
    
    public FinishLine(CanyonMode m, Vector3f position, Material mat) {
        msa = m;
        this.position = position;
        this.mat = mat;
        
        initLine();
        initPhysics();
    }
    
    private void initLine() {
        Box b = new Box(1,1,1);
        nodeFinish = new Node();
        nodeFinish.setLocalTranslation(position);
        geomFinishBox = new Geometry("line finish", b);
//        geomFinishBox.scale(70,5,1); // original finish line dimensions
        geomFinishBox.scale(60,5,1);
        geomFinishBox.setMaterial(mat);
//        geomFinishBox.setCullHint(Spatial.CullHint.Always);
        
        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(-1 * FastMath.PI / 4, Vector3f.UNIT_Y);
        nodeFinish.setLocalRotation(rot);
        geomFinishBox.setMaterial(mat);
        
        nodeFinish.attachChild(geomFinishBox);
        msa.getRootNode().attachChild(nodeFinish);
    }
    
    public void hideLine() {
        msa.bullet.getPhysicsSpace().remove(physFinish);
        geomFinishBox.setCullHint(Spatial.CullHint.Always);
    }
        
    private void initPhysics() {        
        physFinish = new FinishLineGhostControl((CanyonRunMode) msa, new BoxCollisionShape(geomFinishBox.getLocalScale()));
        geomFinishBox.addControl(physFinish);
        msa.bullet.getPhysicsSpace().add(physFinish);
//        physFinish.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_04);
//        physFinish.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
    }
    
    class FinishLineGhostControl extends GhostControl implements PhysicsCollisionListener, PhysicsTickListener {

        private CanyonRunMode msa;
        private String objAName, objBName;
        private PhysicsCollisionEvent event;

        public FinishLineGhostControl(CanyonRunMode m, BoxCollisionShape b) {
            super(b);
            msa = m;
            event = null;
            objAName = "";
            objBName = "";
            msa.bullet.getPhysicsSpace().addCollisionListener(this);
            msa.bullet.getPhysicsSpace().addTickListener(this);
        }

        public void collision(PhysicsCollisionEvent event) {
            this.event = event;
            objAName = event.getNodeA().getName();
            objBName = event.getNodeB().getName();

            if(objAName.equals("Raft") && objBName.equals("line finish")){
                System.out.println("COLLISION " + objAName + " + " + objBName);
            }
            else if(objAName.equals("line finish") && objBName.equals("Raft")){
                System.out.println("COLLISION " + objAName + " + " + objBName);
            }
        }

        public void prePhysicsTick(PhysicsSpace space, float tpf) {
            if(objAName.equals("Raft") && objBName.equals("line finish")){
                System.out.println("Removing collision listener");
                space.removeCollisionListener(this);
                space.removeTickListener(this);
                hideLine();

                if (objBName.equals("line finish")) {
                    msa.stopTimer();
                }
            }
            else if(objAName.equals("line finish") && objBName.equals("Raft")){
                System.out.println("Removing collision listener");
                space.removeCollisionListener(this);
                space.removeTickListener(this);
                hideLine();

                if (objAName.equals("line finish")) {
                    msa.stopTimer();
                }
            }
        }

        public void physicsTick(PhysicsSpace space, float tpf) {
    //        if(getOverlappingObjects().size() > 0) {
    //            System.out.println("overlapping objects: " + getOverlappingObjects().toString());
    //        }
        }
    }
}
