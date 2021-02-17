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
public class Morphology {
    
    public static MyOwnImage Dilation_binary(MyOwnImage img, boolean dilateBackgroundPixel){
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        /**
         * This will hold the dilation result which will be copied to image img.
         */
        int output[] = new int[width * height];
        
        /**
         * If dilation is to be performed on BLACK pixels then
         * targetValue = 0
         * else
         * targetValue = 255;  //for WHITE pixels
         */
        int targetValue = (dilateBackgroundPixel == true)?0:255;
        
        /**
         * If the target pixel value is WHITE (255) then the reverse pixel value will
         * be BLACK (0) and vice-versa.
         */
        int reverseValue = (targetValue == 255)?0:255;
        
        //perform dilation
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                //For BLACK pixel RGB all are set to 0 and for WHITE pixel all are set to 255.
                if(img.getRed(x, y) == targetValue){
                    /**
                     * We are using a 3x3 kernel
                     * [1, 1, 1
                     *  1, 1, 1
                     *  1, 1, 1]
                     */
                    boolean flag = false;   //this will be set if a pixel of reverse value is found in the mask
                    for(int ty = y - 1; ty <= y + 1 && flag == false; ty++){
                        for(int tx = x - 1; tx <= x + 1 && flag == false; tx++){
                            if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                                //origin of the mask is on the image pixels
                                if(img.getRed(tx, ty) != targetValue){
                                    flag = true;
                                    output[x+y*width] = reverseValue;
                                }
                            }
                        }
                    }
                    if(flag == false){
                        //all pixels inside the mask [i.e., kernel] were of targetValue
                        output[x+y*width] = targetValue;
                    }
                }else{
                    output[x+y*width] = reverseValue;
                }
            }
        }
        
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        
        /**
         * Save the dilation value in new image.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }
    
    /**
     * This method will perform dilation operation on the grayscale image img.
     * 
     * @param img The image on which dilation operation is performed
     */
    public static MyOwnImage Dilation_grayscale(MyOwnImage img){
        /**
         * Dimension of the image img.
         */
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        //buff
        int buff[];
        
        //output of dilation
        int output[] = new int[width*height];
        
        //perform dilation
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                buff = new int[9];
                int i = 0;
                for(int ty = y - 1; ty <= y + 1; ty++){
                   for(int tx = x - 1; tx <= x + 1; tx++){
                       if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                           //pixel under the mask
                           buff[i] = img.getRed(tx, ty);
                           i++;
                       }
                   }
                }
                
                //sort buff
                java.util.Arrays.sort(buff);
                
                //save highest value
                output[x+y*width] = buff[8];
            }
        }
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        /**
         * Save the erosion value in image img.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }
    
    /**
     * This method will perform dilation operation on the grayscale image img.
     * It will find the maximum value among the pixels that are under the mask [element value 1] and will
     * set the origin to the maximum value.
     * 
     * @param img The image on which dilation operation is performed
     * @param mask the square mask.
     * @param maskSize the size of the square mask. [i.e., number of rows]
     */
    public static MyOwnImage Dilation_grayscale(MyOwnImage img, int mask[], int maskSize){
        /**
         * Dimension of the image img.
         */
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        //buff
        int buff[];
        
        //output of dilation
        int output[] = new int[width*height];
        
        //perform dilation
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                buff = new int[maskSize * maskSize];
                int i = 0;
                for(int ty = y - maskSize/2, mr = 0; ty <= y + maskSize/2; ty++, mr++){
                   for(int tx = x - maskSize/2, mc = 0; tx <= x + maskSize/2; tx++, mc++){
                       /**
                        * Sample 3x3 mask [kernel or structuring element]
                        * [0, 1, 0
                        *  1, 1, 1
                        *  0, 1, 0]
                        * 
                        * Only those pixels of the image img that are under the mask element 1 are considered.
                        */
                       if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                           //pixel under the mask
                           
                           if(mask[mc+mr*maskSize] != 1){
                               continue;
                           }
                           
                           buff[i] = img.getRed(tx, ty);
                           i++;
                       }
                   }
                }
                
                //sort buff
                java.util.Arrays.sort(buff);
                
                //save lowest value
                output[x+y*width] = buff[(maskSize*maskSize) - 1];
            }
        }
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        /**
         * Save the dilation value in image img.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }

    public static MyOwnImage Erosion_binary(MyOwnImage img, boolean erodeForegroundPixel){
        /**
         * Dimension of the image img.
         */
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        /**
         * This will hold the erosion result which will be copied to image img.
         */
        int output[] = new int[width * height];
        
        /**
         * If erosion is to be performed on BLACK pixels then
         * targetValue = 0
         * else
         * targetValue = 255;  //for WHITE pixels
         */
        int targetValue = (erodeForegroundPixel == true)?0:255;
        
        /**
         * If the target pixel value is WHITE (255) then the reverse pixel value will
         * be BLACK (0) and vice-versa.
         */
        int reverseValue = (targetValue == 255)?0:255;
        
        //perform erosion
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                //For BLACK pixel RGB all are set to 0 and for WHITE pixel all are set to 255.
                if(img.getRed(x, y) == targetValue){
                    /**
                     * We are using a 3x3 kernel
                     * [1, 1, 1
                     *  1, 1, 1
                     *  1, 1, 1]
                     */
                    boolean flag = false;   //this will be set if a pixel of reverse value is found in the mask
                    for(int ty = y - 1; ty <= y + 1 && flag == false; ty++){
                        for(int tx = x - 1; tx <= x + 1 && flag == false; tx++){
                            if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                                //origin of the mask is on the image pixels
                                if(img.getRed(tx, ty) != targetValue){
                                    flag = true;
                                    output[x+y*width] = reverseValue;
                                }
                            }
                        }
                    }
                    if(flag == false){
                        //all pixels inside the mask [i.e., kernel] were of targetValue
                        output[x+y*width] = targetValue;
                    }
                }else{
                    output[x+y*width] = reverseValue;
                }
            }
        }
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        /**
         * Save the erosion value in image img.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }
    
    /**
     * This method will perform erosion operation on the grayscale image img.
     * 
     * @param img The image on which erosion operation is performed
     */
    public static MyOwnImage Erosion_grayscale(MyOwnImage img){
        /**
         * Dimension of the image img.
         */
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        //buff
        int buff[];
        
        //output of erosion
        int output[] = new int[width*height];
        
        //perform erosion
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                buff = new int[9];
                int i = 0;
                for(int ty = y - 1; ty <= y + 1; ty++){
                   for(int tx = x - 1; tx <= x + 1; tx++){
                       /**
                        * 3x3 mask [kernel or structuring element]
                        * [1, 1, 1
                        *  1, 1, 1
                        *  1, 1, 1]
                        */
                       if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                           //pixel under the mask
                           buff[i] = img.getRed(tx, ty);
                           i++;
                       }
                   }
                }
                
                //sort buff
                java.util.Arrays.sort(buff);
                
                //save lowest value
                output[x+y*width] = buff[9-i];
            }
        }
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        /**
         * Save the erosion value in image img.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }
    
    /**
     * This method will perform erosion operation on the grayscale image img.
     * It will find the minimum value among the pixels that are under the mask [element value 1] and will
     * set the origin to the minimum value.
     * 
     * @param img The image on which erosion operation is performed
     * @param mask the square mask.
     * @param maskSize the size of the square mask. [i.e., number of rows]
     */
    public static MyOwnImage Erosion_grayscale(MyOwnImage img, int mask[], int maskSize){
        /**
         * Dimension of the image img.
         */
        int width = img.getImageWidth();
        int height = img.getImageHeight();
        
        //buff
        int buff[];
        
        //output of erosion
        int output[] = new int[width*height];
        
        //perform erosion
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                buff = new int[maskSize * maskSize];
                int i = 0;
                for(int ty = y - maskSize/2, mr = 0; ty <= y + maskSize/2; ty++, mr++){
                   for(int tx = x - maskSize/2, mc = 0; tx <= x + maskSize/2; tx++, mc++){
                       /**
                        * Sample 3x3 mask [kernel or structuring element]
                        * [0, 1, 0
                        *  1, 1, 1
                        *  0, 1, 0]
                        * 
                        * Only those pixels of the image img that are under the mask element 1 are considered.
                        */
                       if(ty >= 0 && ty < height && tx >= 0 && tx < width){
                           //pixel under the mask
                           
                           if(mask[mc+mr*maskSize] != 1){
                               continue;
                           }
                           
                           buff[i] = img.getRed(tx, ty);
                           i++;
                       }
                   }
                }
                
                //sort buff
                java.util.Arrays.sort(buff);
                
                //save lowest value
                output[x+y*width] = buff[(maskSize*maskSize) - i];
            }
        }
        MyOwnImage image = new MyOwnImage(img); // Create a copy in order to work on copied one
        /**
         * Save the erosion value in image img.
         */
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int v = output[x+y*width];
                image.setPixel(x, y, 255, v, v, v);
            }
        }
        return image;
    }
}