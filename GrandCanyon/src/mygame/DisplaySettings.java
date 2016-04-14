/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;
import world.World;

/**
 *
 * @author Graeme
 */
public class DisplaySettings {
    public static int screenX;
    public static int screenY;
    
    private DirectionalLight mainLight;
    
    SimpleApplication  sa;
    
    public DisplaySettings(SimpleApplication sa, World w){
        this.sa = sa;
        
        initLights();
        initFlyCam(w);
        initCoordCross();
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
    
    public void initFlyCam(World w){
        Camera cam = sa.getCamera();
        sa.getFlyByCamera().setMoveSpeed(50.0f);
//        cam.setLocation(new Vector3f(5f, w.getWaterHeight() + 5f, 5f));
//        cam.lookAt(new Vector3f(0f, w.getWaterHeight() + 1f, 20f), Vector3f.UNIT_Y);
        
        // looks at start line
//        cam.setLocation(new Vector3f(-190f, w.getWaterHeight() + 20f, -100f));
//        cam.lookAt(new Vector3f(-190f, w.getWaterHeight(), -135f), Vector3f.UNIT_Y);
        
        // looks at finish line
        cam.setLocation(new Vector3f(2090f, w.getWaterHeight() + 20f, 845f));
        cam.lookAt(new Vector3f(2090f, w.getWaterHeight(), 820f), Vector3f.UNIT_Y);
    }
    
    private void initLights(){
        
        mainLight = new DirectionalLight();
        mainLight.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        mainLight.setColor(ColorRGBA.White);
        sa.getRootNode().addLight(mainLight);
    }
    
    public DirectionalLight getMainLight(){
        return mainLight;
    }
    
    private void initCoordCross() {
        Material matRed, matGreen, matBlue;
        matRed = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matRed.setColor("Color", ColorRGBA.Red);
        matGreen = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matGreen.setColor("Color", ColorRGBA.Green);
        matBlue = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matBlue.setColor("Color", ColorRGBA.Blue);
        //
        Line xAxis = new Line(new Vector3f(-3, 0, 0), Vector3f.ZERO);
        Line yAxis = new Line(new Vector3f(0, -3, 0), Vector3f.ZERO);
        Line zAxis = new Line(new Vector3f(0, 0, -3), Vector3f.ZERO);
        Arrow ax = new Arrow(new Vector3f(3, 0, 0));
        Arrow ay = new Arrow(new Vector3f(0, 3, 0));
        Arrow az = new Arrow(new Vector3f(0, 0, 3));
        Geometry geomX = new Geometry("xAxis", xAxis);
        Geometry geomY = new Geometry("yAxis", yAxis);
        Geometry geomZ = new Geometry("zAxis", zAxis);
        geomX.setMaterial(matRed);
        geomY.setMaterial(matGreen);
        geomZ.setMaterial(matBlue);
        Geometry geomXA = new Geometry("xAxis", ax);
        Geometry geomYA = new Geometry("yAxis", ay);
        Geometry geomZA = new Geometry("zAxis", az);
        geomXA.setMaterial(matRed);
        geomYA.setMaterial(matGreen);
        geomZA.setMaterial(matBlue);
        sa.getRootNode().attachChild(geomX);
        sa.getRootNode().attachChild(geomY);
        sa.getRootNode().attachChild(geomZ);
        sa.getRootNode().attachChild(geomXA);
        sa.getRootNode().attachChild(geomYA);
        sa.getRootNode().attachChild(geomZA);
    }
}