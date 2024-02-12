# Scala-LLVM

LLVM bindings for Scala.

## Dependencies

* CMake
* LLVM 15.0.7

## Configuration

```bash
# Initialize the native build tool.
#
# Important: This step re-generates `CMakeLists.txt`! Then this file must be
# updated to setup LLVM (see https://llvm.org/docs/CMake.html).
sbt "nativeInit cmake scalallvm"

# Generate native headers.
#
# This step updates `native/src/include/llvm_LLVM_00024.h`
sbt javah

# Compile the bindings.
sbt nativeCompile

# Run the core target.
sbt core/run
```
