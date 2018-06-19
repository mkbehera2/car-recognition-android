//#define DLIB_JPEG_SUPPORT // this should work according to library specification

#include <jni.h>
#include <string>
#include "dlib/svm.h"
#include <iostream>
#include "dlib/dnn.h"
#include "dlib/image_io.h"
#include "dlib/gui_widgets.h"
#include "dlib/image_processing.h"
#include <android/log.h>


using namespace std;
using namespace dlib;

// The front and rear view vehicle detector network
template <long num_filters, typename SUBNET> using con5d = con<num_filters,5,5,2,2,SUBNET>;
template <long num_filters, typename SUBNET> using con5  = con<num_filters,5,5,1,1,SUBNET>;
template <typename SUBNET> using downsampler  = relu<affine<con5d<32, relu<affine<con5d<32, relu<affine<con5d<16,SUBNET>>>>>>>>>;
template <typename SUBNET> using rcon5  = relu<affine<con5<55,SUBNET>>>;
using net_type = loss_mmod<con<1,9,9,1,1,rcon5<rcon5<rcon5<downsampler<input_rgb_image_pyramid<pyramid_down<6>>>>>>>>;

extern "C" JNIEXPORT jstring JNICALL
Java_co_netguru_android_carrecognition_feature_main_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_co_netguru_android_carrecognition_feature_main_MainActivity_findCarsJNI(
        JNIEnv *env,
        jobject /*this*/,jstring modelFileNamePath,jstring imageFileNamePath) {

    string model_file_path;
    const char *c_model_file_path;

    string image_file_path;
    const char *c_image_file_path;

    try {

        net_type net;
        shape_predictor sp;
        // You can get this file from http://dlib.net/files/mmod_front_and_rear_end_vehicle_detector.dat.bz2
        // This network was produced by the dnn_mmod_train_find_cars_ex.cpp example program.
        // As you can see, the file also includes a separately trained shape_predictor.  To see
        // a generic example of how to train those refer to train_shape_predictor_ex.cpp.

        c_model_file_path = env->GetStringUTFChars(modelFileNamePath, NULL);
        model_file_path = string(c_model_file_path);

        c_image_file_path = env->GetStringUTFChars(imageFileNamePath, NULL);
        image_file_path = string(c_image_file_path);

        __android_log_write(ANDROID_LOG_DEBUG, "Model File Path", c_model_file_path);
        __android_log_write(ANDROID_LOG_DEBUG, "Image File Path", c_image_file_path);

        deserialize(model_file_path) >> net >> sp;

        matrix <rgb_pixel> img;
        load_image(img, image_file_path); // todo in this place the load bmp error occurs


        //image_window win;
        //win.set_image(img);

        // Run the detector on the image and show us the output.
     //   for (auto &&d : net(img)) {
            // We use a shape_predictor to refine the exact shape and location of the detection
            // box.  This shape_predictor is trained to simply output the 4 corner points of
            // the box.  So all we do is make a rectangle that tightly contains those 4 points
            // and that rectangle is our refined detection position.
           // auto fd = sp(img, d);
           // rectangle rect;
          //  for (unsigned long j = 0; j < fd.num_parts(); ++j)
         //       rect += fd.part(j);

         //   if (d.label == "rear")
                //win.add_overlay(rect, rgb_pixel(255, 0, 0), d.label);
          //  else
                //win.add_overlay(rect, rgb_pixel(255, 255, 0), d.label);
        //}
        string ok = " OK :):):) ";
        return env->NewStringUTF(ok.c_str());
    } catch(image_load_error& e)
    {
        return env->NewStringUTF(e.info.c_str());
    }
    catch(serialization_error& e)
    {
        const char* error = "Error loading model file";
        char* name_with_extension;
        name_with_extension = static_cast<char *>(malloc(strlen(error) + strlen(c_model_file_path)));
        strcpy(name_with_extension, error); /* copy name into the new var */
        strcat(name_with_extension, c_model_file_path); /* add the path */
        return env->NewStringUTF(name_with_extension);
    }
    catch(std::exception& e)
    {
        string error = "exception";
        return env->NewStringUTF(error.c_str());
    }
}


