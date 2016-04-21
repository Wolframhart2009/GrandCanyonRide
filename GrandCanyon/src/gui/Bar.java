/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author Graeme
 */
public class Bar extends Node{
    
    Application sa;
    private Node pNode;
    private float x, y;
    private float height, width;
    private int size;
    private boolean alpha;
    
    private Picture[] states;
    private int curInitLoc;
    private int curPlace;

    public Bar(Application sa, Node parent, float x, float y, float height, float width, int size, boolean useAlpha){
        this.sa = sa;
        this.pNode = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.size = size;
        this.alpha = useAlpha;
        
        states = new Picture[size];
        initStates();
        curInitLoc = 0;
        
        this.setLocalTranslation(x, y, 0);
    }
    
    private void initStates(){
        for(int i = 0; i < size; i++){
            states[i] = new Picture("image_" + i);
        }
    }
    
    public int initTotal(String imPath, String ext){
        int success = 1;
        
        if(curInitLoc != 0){
            return 0;
        }
        
        for(int i =0; i < size || success != 1; i++, curInitLoc++){
            try{
                states[i].setImage(sa.getAssetManager(), imPath + "_" + i + ext, alpha);
                states[i].setHeight(height);
                states[i].setWidth(width);
                
            }
            catch(Exception e){
                System.out.println("Error occured setting bar state " + i);
                success = 0;
                this.reset();
                break;
            }
        }
        
        
        return success;
    }
    
    public int initNext(String imPath){
        int success = 1;
        
        if(curInitLoc == size){
            success = 0;
        }
        else{
            try{
                states[curInitLoc].setImage(sa.getAssetManager(), imPath, alpha);
                states[curInitLoc].setHeight(height);
                states[curInitLoc].setWidth(width);
                curInitLoc++;
            }
            catch(Exception e){
                success = 0;
                states[curInitLoc] = null;
                states[curInitLoc] = new Picture("Image_" + curInitLoc);
            }
        }
        
        return success;
    }
    
    public void reset(){
        for(int i = 0; i < size; i++){
            states[i] = null;
        }
        initStates();
    }
    
    public void finish(){
        this.pNode.attachChild(this);
    }
    
    public int setCurrentDisplay(int place){
        int success = 1;
        if(place < 0 || place >= size){
            success = 0;
        }
        else{
            curPlace = place;
            this.detachAllChildren();
            this.attachChild(states[place]);
        }
        return success;
    }
    
    public int getCurPlace(){
        return curPlace;
    }
}
