import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import cv2
import tensorflow as tf
from PIL import Image
import os
from sklearn.model_selection import train_test_split
from keras.utils import to_categorical
from keras.models import Sequential, load_model
from keras.layers import Conv2D, MaxPool2D, Dense, Flatten, Dropout
import tqdm
import warnings


data = []
labels = []
classes = 43

for i in range(classes):
    path = os.path.join(os.getcwd(),'train',str(i))
    images = os.listdir(path)

    for j in images:
        try:
            image = Image.open(path + '/'+ j)
            image = image.resize((30,30))
            image = np.array(image)
            data.append(image)
            labels.append(i)
        except:
            print("Error loading image")
#Converting lists into numpy arrays bcoz its faster and takes lesser
#memory
data = np.array(data)
labels = np.array(labels)
print(data.shape, labels.shape)

X_train, X_test, y_train, y_test = train_test_split(data, labels, test_size=0.2, random_state=68)

print(X_train.shape, X_test.shape, y_train.shape, y_test.shape)

model = Sequential()
model.add(Conv2D(filters=32, kernel_size=(5,5), activation='relu', input_shape=X_train.shape[1:]))
model.add(Conv2D(filters=32, kernel_size=(5,5), activation='relu'))
model.add(MaxPool2D(pool_size=(2, 2)))
model.add(Dropout(rate=0.25))
model.add(Conv2D(filters=64, kernel_size=(3, 3), activation='relu'))
model.add(Conv2D(filters=64, kernel_size=(3, 3), activation='relu'))
model.add(MaxPool2D(pool_size=(2, 2)))
model.add(Dropout(rate=0.25))
model.add(Flatten())
model.add(Dense(256, activation='relu'))
model.add(Dropout(rate=0.5))
model.add(Dense(43, activation='sigmoid'))

model.compile(loss='sparse_categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
history = model.fit(X_train, y_train, batch_size=100, epochs=2, validation_data=(X_test,y_test)) #model.save("Trafic_signs_model.h5")

#plotting graphs for accuracy
plt.figure(0)
plt.plot(history.history['accuracy'], label='training accuracy')
plt.plot(history.history['val_accuracy'], label='val accuracy')
plt.title('Accuracy')
plt.xlabel('epochs')
plt.ylabel('accuracy')
plt.legend()
plt.show()
#plotting graphs for loss
plt.figure(1)
plt.plot(history.history['loss'], label='training loss')
plt.plot(history.history['val_loss'], label='val loss')
plt.title('Loss')
plt.xlabel('epochs')
plt.ylabel('loss')
plt.legend()
plt.show()


# from sklearn.metrics import accuracy_score
# y_test = pd.read_csv('Test.csv')
#
# labels = y_test["ClassId"].values
# imgs = y_test["Path"].values
#
# data=[]
#
# for img in imgs:
#     image = Image.open(img)
#     image = image.resize((30,30))
#     data.append(np.array(image))
#
# X_test=np.array(data)
#
# predict = np.argmax(model.predict(X_test), axis=1)
# #Accuracy with the test data
# print(accuracy_score(labels, predict))


# import cv2
# cap = cv2.VideoCapture('/Users/pro/Desktop/Neural_Network/video2.mp4')
#
# if (cap.isOpened()== False):
#   print("Error opening video  file")
#
# while(cap.isOpened()):
#
#
#   ret, frame = cap.read()
#   if ret == True:
#
#
#     cv2.imshow('Frame', frame)
#
#
#     if cv2.waitKey(25) & 0xFF == ord('q'):
#       break
#
#
#   else:
#     break
