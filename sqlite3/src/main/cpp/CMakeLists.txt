cmake_minimum_required(VERSION 3.4.1)
project(foobar)

list(APPEND EXT_COMPILER
        ${CMAKE_CXX_COMPILER}
        --target=${CMAKE_CXX_COMPILER_TARGET}
        --gcc-toolchain=${CMAKE_CXX_COMPILER_EXTERNAL_TOOLCHAIN}
        --sysroot=${CMAKE_SYSROOT})
string(REPLACE ";" " " EXT_COMPILER "${EXT_COMPILER}")

list(APPEND EXT_CFLAGS
        ${CMAKE_CXX_FLAGS}
        )
string(REPLACE ";" " " EXT_CFLAGS "${EXT_CFLAGS}")

list(APPEND EXT_LDFLAGS
        ${CMAKE_SHARED_LINKER_FLAGS})
string(REPLACE ";" " " EXT_LDFLAGS "${EXT_LDFLAGS}")
include(ProcessorCount)
ProcessorCount(nproc)
include(ExternalProject)
message("-----------------------------------------------------------------------------------------")
message(${nproc})
message(${EXT_COMPILER})
message(${EXT_CFLAGS})
message(${CMAKE_CURRENT_BINARY_DIR}/gmp_ext-prefix/src/gmp_ext-build)
message("-----------------------------------------------------------------------------------------")
ExternalProject_add(
        gmp_ext
        BUILD_BYPRODUCTS ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libgmp.so
        ${CMAKE_CURRENT_BINARY_DIR}/gmp_ext-prefix/src/gmp_ext-build/gmp.h

        URL https://gmplib.org/download/gmp/gmp-6.1.2.tar.bz2
        SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/gmp

        CONFIGURE_COMMAND ${CMAKE_CURRENT_SOURCE_DIR}/gmp/configure
        --host=${CMAKE_CXX_COMPILER_TARGET} --with-sysroot=${CMAKE_SYSROOT} --disable-static
        CC=${EXT_COMPILER}
        CFLAGS=${EXT_CFLAGS}
        LDFLAGS=${EXT_LDFLAGS}

        BUILD_COMMAND make -j${nproc}

        INSTALL_COMMAND cp .libs/libgmp.so ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
)
add_library(gmp SHARED IMPORTED)
add_dependencies(gmp gmp_ext)
set_target_properties(gmp
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libgmp.so)
add_subdirectory(sqlite)
#add_dependencies(sqlite gmp)
target_include_directories(sqlite PUBLIC ${CMAKE_CURRENT_BINARY_DIR}/gmp_ext-prefix/src/gmp_ext-build)
