project(sqliteX)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
add_definitions(
        -DHAVE_USLEEP=1
        -DSQLITE_ENABLE_FTS5
        -DSQLITE_ENABLE_RTREE
        -DSQLITE_ENABLE_JSON1
        -DSQLITE_ENABLE_FTS3
        -DSQLITE_ENABLE_BATCH_ATOMIC_WRITE
        -DSQLITE_TEMP_STORE=3

        -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
        -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
        -U__APPLE__
        -DHAVE_STRCHRNUL=0
        -DSQLITE_USE_URI=1
        -Wno-unused-parameter -Wno-int-to-pointer-cast
        -Wno-uninitialized -Wno-parentheses
        -Wno-conversion-null
)


if ($(TARGET_ARCH) STREQUAL arm)
    add_definitions(-DPACKED="__attribute__ ((packed))")
else ()
    add_definitions(-DPACKED="")
endif ()

add_library(sqliteX
        SHARED
        android_database_SQLiteCommon.cpp
        android_database_SQLiteConnection.cpp
        android_database_SQLiteGlobal.cpp
        android_database_SQLiteDebug.cpp
        JNIHelp.cpp
        JniConstants.cpp
        sqlite3.c
        )
target_include_directories(sqliteX PUBLIC nativehelper/)

find_library(log-lib log)
find_library(dl-lib dl)

target_link_libraries(sqliteX ${log-lib})
target_link_libraries(sqliteX ${dl-lib})
target_link_libraries(sqliteX gmp)
