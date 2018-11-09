import cv2
import os
import glob
from PIL import Image

def converter(filename):
    path = os.path.dirname(filename) + '/' + os.path.basename(filename) 
    data_path = os.path.join(path,'*')
    files = glob.glob(data_path)

    new_path = os.path.dirname(filename) + '/' + os.path.basename(filename) + os.path.basename(filename) + '_Converted_to_Jpgs'
    print(new_path)
    if not os.path.exists(new_path):
        os.makedirs(new_path)
    for f1 in files:
        if(os.path.isdir(f1)):
            converter(os.path.dirname(f1) + '/' + os.path.basename(f1))
        try:

            with Image.open(f1) as imgg:
        
                if(imgg.format != 'RGB'):    
                    rgb_im = imgg.convert('RGB')

                else:
                    rgb_im = imgg    
                
                str1 = os.path.basename(f1)
                str2 = os.path.splitext(str1)[0] 
                rgb_im.save(new_path +'/' + str2  + '.jpg')    
        except IOError as e:
            print(e)
            pass      


# img_dir = "Users/gerardus/Documents/20th_Century_fox/MSFT-Hack1/Code/downloads/" # Enter Directory of all images 
        
# converter(img_dir)

    