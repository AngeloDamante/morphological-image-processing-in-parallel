/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallel_java;
//import java.lang.*;

/**
 *
 * @author Fabian Greavu
 */
public class MaskApplier extends Thread{
    private MyOwnImage img;
    private int output[];
    private int width;
    private int height;
    private int y_min;
    private int y_max;
    private int mask[];
    private int maskSize;
    private MorphOp operation;//True if Dilation, false if erosion
    //private int buff[];
    private int curr_pixel_val;
    private int image_pixel_val;
    private int x, y, tx, ty, mr, mc;
    
    public MaskApplier(MyOwnImage img, int output[], int y_min, int y_max, int width, int height, int mask[], int maskSize, MorphOp operation){
        this.img = img;
        this.output = output;
        this.width = width;
        this.height = height;
        this.y_min = y_min;
        this.y_max = y_max;
        //TODO: insert future check on square matrix and odd
        this.mask = mask;
        this.maskSize = maskSize;
        this.operation=operation;
        //this.buff = new int[9];
        this.curr_pixel_val = 0;
        this.image_pixel_val = 0;
    }
    
    @Override
    public void run(){
        ApplyMask();
    }
    
    public void ApplyMask(){
        for(y = y_min; y < y_max; y++){
            for(x = 0; x < width; x++){
                //buff
                //int i = 0;
                curr_pixel_val = img.getRed(x, y);
                for(ty = y - maskSize/2, mr = 0; ty <= y + maskSize/2; ty++, mr++){
                   for(tx = x - maskSize/2, mc = 0; tx <= x + maskSize/2; tx++, mc++){
                       if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                           if(mask[mc+mr*maskSize] != 1){
                               continue;
                           }
                           //pixel under the mask
                           //buff[i] = img.getRed(tx, ty);
                           //i++;
                           image_pixel_val = img.getRed(tx, ty);
                           if(this.operation==MorphOp.Dilation){
                               if(image_pixel_val > curr_pixel_val)
                                   curr_pixel_val = image_pixel_val;
                           }else{
                               if(image_pixel_val < curr_pixel_val)
                                   curr_pixel_val = image_pixel_val;
                           }
                       }
                   }
                }

                //sort buff
                //java.util.Arrays.sort(buff);
                //output[x+y*width] = this.operation==MorphOp.Dilation ? buff[(maskSize*maskSize) - 1] : buff[(maskSize*maskSize) - i];
                output[x+y*width] = curr_pixel_val;
            }
        }
    }
        
}
