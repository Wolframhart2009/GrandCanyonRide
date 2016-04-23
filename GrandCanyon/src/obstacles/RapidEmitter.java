/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obstacles;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import mygame.CanyonMode;

/**
 *
 * @author Graeme
 */
public class RapidEmitter extends ParticleEmitter{    
    CanyonMode sa;
    
    Node parNode;
    
    public RapidEmitter(CanyonMode s, Node p){
        super("Rapid_Spray_Emitter", ParticleMesh.Type.Triangle, 30);
        
        this.sa = s;
        this.parNode = p;
        
        initGraphics();
        addToScene();
    }
    
    private void initGraphics(){
        Material rapidMat = new Material(this.sa.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        rapidMat.setTexture("Texture", this.sa.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
        this.setMaterial(rapidMat);
        
        this.setImagesX(2);
        this.setImagesY(2); 
        
        this.setEndColor( new ColorRGBA(0f, 0f, 1f, 1f));   // Blue to
        this.setStartColor(new ColorRGBA(1f, 1f, 1f, 0.5f)); // White
        
        this.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 6,0));
        
        this.setStartSize(1.5f);
        this.setEndSize(0.1f);
        this.setGravity(0,1,0);
        
        this.setParticlesPerSec(5.0f);
        this.setLowLife(0.5f);
        this.setHighLife(3f);
        this.getParticleInfluencer().setVelocityVariation(0.3f);
    }
    
    private void addToScene(){
        parNode.attachChild(this);
    }
}
