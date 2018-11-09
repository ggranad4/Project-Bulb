import jpgConverter
import os

from google_images_download import google_images_download   #importing the library

response = google_images_download.googleimagesdownload()   #class instantiation

arguments = {"keywords":"Polar bears,beer,cars","limit":10,"print_urls":False}   #creating list of arguments
paths = response.download(arguments)   #passing the arguments to the function
path = str(paths)
pathv2 = os.path.dirname(path)

pathv3 = pathv2.split("/")
length = len(pathv3) - 2
directory = '/'
final_directory = []
final_directory.append('/')

count = 0

for x in range(len(pathv3)):
   if (x == 0 or x == length):
         print('')
   elif(pathv3[x] =='downloads' and count == 0):
       count+=1
       final_directory[0].replace(''    , '')
       final_directory.append(pathv3[x])
      
   elif(count == 0):
        directory += pathv3[x] + '/'
        final_directory.append(pathv3[x]+ '/')
   
     
jpgConverter.converter(''.join(final_directory))       



