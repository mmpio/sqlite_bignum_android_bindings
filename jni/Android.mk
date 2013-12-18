LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

ANDROIDSRC = /home/dan/big/Android

JNI_H_INCLUDE = $(ANDROIDSRC)/libnativehelper/include/nativehelper/

LOCAL_CFLAGS += -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
LOCAL_CFLAGS += -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
LOCAL_CFLAGS += -U__APPLE__
LOCAL_CFLAGS += -Wno-unused-parameter -Wno-int-to-pointer-cast
LOCAL_CFLAGS += -Wno-maybe-uninitialized -Wno-parentheses
LOCAL_CPPFLAGS += -Wno-conversion-null

ifeq ($(TARGET_ARCH), arm)
	LOCAL_CFLAGS += -DPACKED="__attribute__ ((packed))"
else
	LOCAL_CFLAGS += -DPACKED=""
endif

LOCAL_SRC_FILES:= \
	android_database_SQLiteCommon.cpp \
	android_database_SQLiteConnection.cpp \
	android_database_SQLiteGlobal.cpp \
	android_database_SQLiteDebug.cpp  \
	sqlite3.c

LOCAL_SRC_FILES += \
	JNIHelp.cpp \
	JniConstants.cpp 


LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE) \
	$(ANDROIDSRC)/system/core/include/ \
	$(ANDROIDSRC)/frameworks/base/include/ \
	$(ANDROIDSRC)/frameworks/native/include/ \
	$(ANDROIDSRC)/libnativehelper/include/ \
	$(ANDROIDSRC)/frameworks/base/core/jni \


LOCAL_MODULE:= libsqliteX

LOCAL_LDLIBS += -ldl -llog 
# LOCAL_LDLIBS += -lnativehelper -landroid_runtime -lutils -lbinder

include $(BUILD_SHARED_LIBRARY)

