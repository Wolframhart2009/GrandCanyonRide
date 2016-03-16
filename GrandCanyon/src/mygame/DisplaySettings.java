/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Graeme
 */
public class DisplaySettings {
    public static int screenX;
    public static int screenY;
    
    SimpleApplication  sa;
    
    public DisplaySettings(SimpleApplication sa){
        this.sa = sa;
        
        initLights();
        initCam();
    }

    public static void initDisplay(SimpleApplication s){
        AppSettings aps = new AppSettings(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.width *= 0.75;
        screen.height *= 0.75;
        
        screenX = screen.width;
        screenY = screen.height;
        
        aps.setResolution(screen.width, screen.height);
        s.setSettings(aps);
        s.setShowSettings(false);
        s.setDisplayStatView(false);
    }
    
    public void initCam(){
        //Cam code goes here
    }
    
    public void initLights(){
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        sa.getRootNode().addLight(sun);
    }
}