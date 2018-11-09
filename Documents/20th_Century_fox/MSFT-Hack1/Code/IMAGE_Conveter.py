import cv2
import os
import glob
from PIL import Image

img_dir = "/Users/gerardus/Downloads/tester" # Enter Directory of all images 
data_path = os.path.join(img_dir,'*')
files = glob.glob(data_path)
data = []
count = 0
image_list = []

directory = img_dir + '/' + 'pics' + '/'
if not os.path.exists(directory):
    os.makedirs(directory)
path = directory

for f1 in files:
    try:
        with Image.open(f1) as imgg:
            
            if(imgg.format != 'RGB'):    
                rgb_im = imgg.convert('RGB')

            else:
                rgb_im = imgg    
            
            str1 = os.path.basename(f1)
            str2 = os.path.splitext(str1)[0] 
            rgb_im.save(path + str2 +  '.jpg')    
    except IOError as e:
        print(e)
        pass        
    
    # imgg = Image.open(f1)
    # rgb_im = imgg.convert('RGB')
    # str_count =  str(count)
    # count+= 1
    # fileName = str_count + "beer"
    # rgb_im.save(fileName+ '.jpg')
    # filename1 = rgb_im.save(fileName+ '.jpg')
    
   
    

    