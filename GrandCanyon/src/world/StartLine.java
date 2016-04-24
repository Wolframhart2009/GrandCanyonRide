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
public class StartLine extends CourseLine {
    private CanyonMode msa;
    private Vector3f position;
    private Material mat;
    
    private Node nodeStart;
    private Geometry geomStartBox;
    private GhostControl physStart;
    
    public StartLine(CanyonMode m, Vector3f position, Material mat) {
        msa = m;
        this.position = position;
        this.mat = mat;
        
        initLine();
        initPhysics();
    }

    private void initLine() {
        Box b = new Box(1,1,1); 
        nodeStart = new Node();
        nodeStart.setLocalTranslation(position);
        geomStartBox = new Geometry("line start", b);
        geomStartBox.scale(60,5,1);
        geomStartBox.setMaterial(mat);
        
        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(-1 * FastMath.PI / 16, Vector3f.UNIT_Y);
        nodeStart.setLocalRotation(rot);
        
        nodeStart.attachChild(geomStartBox);
        msa.getRootNode().attachChild(nodeStart);
    }
    
    public void hideLine() {
        msa.bullet.getPhysicsSpace().remove(physStart);
        geomStartBox.setCullHint(Spatial.CullHint.Always);
    }
    
    private void initPhysics() {
        physStart = new StartLineGhostControl((CanyonRunMode) msa, new BoxCollisionShape(geomStartBox.getLocalScale()));
        geomStartBox.addControl(physStart);
        msa.bullet.getPhysicsSpace().add(physStart);
//        physStart.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_04);
//        physStart.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
    }
    
    class StartLineGhostControl extends GhostControl implements PhysicsCollisionListener, PhysicsTickListener {

        private CanyonRunMode msa;
        private String objAName, objBName;
        private PhysicsCollisionEvent event;

        public StartLineGhostControl(CanyonRunMode m, BoxCollisionShape b) {
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

            if(objAName.equals("Raft") && objBName.equals("line start")){
                System.out.println("COLLISION " + objAName + " + " + objBName);
            }
            else if(objAName.equals("line start") && objBName.equals("Raft")){
                System.out.println("COLLISION " + objAName + " + " + objBName);
            }
        }

        public void prePhysicsTick(PhysicsSpace space, float tpf) {
            if(objAName.equals("Raft") && objBName.equals("line start")){
                System.out.println("Removing collision listener");
                space.removeCollisionListener(this);
                space.removeTickListener(this);
                hideLine();

                if(objBName.equals("line start")) {
                    msa.startTimer();
                }
            }
            else if(objAName.equals("line start") && objBName.equals("Raft")){
                System.out.println("Removing collision listener");
                space.removeCollisionListener(this);
                space.removeTickListener(this);
                hideLine();

                if(objAName.equals("line start")) {
                    msa.startTimer();
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
