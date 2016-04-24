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
import mygame.CanyonMode;
import mygame.CanyonRunMode;

/**
 *
 * @author Robyn
 */
public class CourseLineGhostControl extends GhostControl implements PhysicsCollisionListener, PhysicsTickListener {

    private CanyonRunMode msa;
    private Course course;
    private String objAName, objBName;
    private PhysicsCollisionEvent event;
    
    public CourseLineGhostControl(CanyonRunMode m, Course course, BoxCollisionShape b) {
        super(b);
        msa = m;
        this.course = course;
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
        
        if(objAName.equals("Raft") && objBName.startsWith("line")){
            System.out.println("COLLISION " + objAName + " + " + objBName);
        }
        else if(objAName.equals("line") && objBName.equals("Raft")){
            System.out.println("COLLISION " + objAName + " + " + objBName);
        }
    }

    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        if(objAName.equals("Raft") && objBName.startsWith("line")){
            System.out.println("Removing collision listener");
            space.removeCollisionListener(this);
            space.removeTickListener(this);
            course.hideLine(objBName);
            
            if(objBName.equals("line start")) {
                msa.startTimer();
            } else if (objBName.equals("line finish")) {
                msa.stopTimer();
            }
        }
        else if(objAName.startsWith("line") && objBName.equals("Raft")){
            System.out.println("Removing collision listener");
            space.removeCollisionListener(this);
            space.removeTickListener(this);
            course.hideLine(objAName);
            
            if(objAName.equals("line start")) {
                msa.startTimer();
            } else if (objAName.equals("line finish")) {
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