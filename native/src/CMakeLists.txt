################################################################
# A minimal CMake file that is compatible with sbt-jni         #
#                                                              #
# All settings required by sbt-jni have been marked so, please #
# add/modify/remove settings to build your specific library.   #
################################################################

cmake_minimum_required(VERSION 3.12)

option(SBT "Set if invoked from sbt-jni" OFF)

# Define project and related variables
# (required by sbt-jni) please use semantic versioning
#
project (scalallvm)
set(PROJECT_VERSION_MAJOR 0)
set(PROJECT_VERSION_MINOR 0)
set(PROJECT_VERSION_PATCH 0)

set(CMAKE_CXX_STANDARD 20)

# Setup JNI
find_package(JNI REQUIRED)
message (STATUS "JNI include directories: ${JNI_INCLUDE_DIRS}")

include_directories(${JNI_INCLUDE_DIRS})

# Setup LLVM
set(LLVM_DIR "/opt/local/libexec/llvm-15/lib/cmake/llvm")
find_package(LLVM REQUIRED CONFIG)
message(STATUS "Found LLVM ${LLVM_PACKAGE_VERSION}")
message(STATUS "Using LLVMConfig.cmake in: ${LLVM_DIR}")

include_directories(${LLVM_INCLUDE_DIRS})
separate_arguments(LLVM_DEFINITIONS_LIST NATIVE_COMMAND ${LLVM_DEFINITIONS})
add_definitions(${LLVM_DEFINITIONS_LIST})

llvm_map_components_to_libnames(llvm_libs ${LLVM_TARGETS_TO_BUILD} core codegen support bitwriter)

# Include directories
include_directories(.)
include_directories(include)

# Flags
# set(CMAKE_CXX_FLAGS "-std=c++20")

# Sources
file(GLOB LIB_SRC
  "*.c"
  "*.cc"
  "*.cpp"
)

# Setup installation targets
# (required by sbt-jni) major version should always be appended to library name
#
set (LIB_NAME ${PROJECT_NAME}${PROJECT_VERSION_MAJOR})
add_library(${LIB_NAME} SHARED ${LIB_SRC})
target_link_libraries(${LIB_NAME} ${llvm_libs})
install(TARGETS ${LIB_NAME} LIBRARY DESTINATION .)
