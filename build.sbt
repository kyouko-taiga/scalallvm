import com.github.sbt.jni.build.CMake

scalaVersion := "3.3.1"
classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat

// nativeBuildTool := CMake.make(Seq(
//   "-DCMAKE_BUILD_TYPE=Release",
//   "-DSBT:BOOLEAN=true",
//   "-DLLVM_DIR=/opt/local/libexec/llvm-16/lib/cmake/llvm"))

lazy val root = (project in file(".")).aggregate(core, native)

lazy val core = project
  .settings(
    javah / target := (native / nativeCompile / sourceDirectory).value / "include",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test)
  .dependsOn(native % Runtime) // remove this if `core` is a library, leave choice to end-user

lazy val native = project
  .settings(nativeCompile / sourceDirectory := sourceDirectory.value)
  .enablePlugins(JniNative) // JniNative needs to be explicitly enabled
