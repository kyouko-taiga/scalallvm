#ifndef SCALALLVM_TAGGED_POINTER_HH
#define SCALALLVM_TAGGED_POINTER_HH

#include <concepts>
#include <cstdlib>

namespace scalallvm {

/// Namespace for utilities related to the use of fat tagged pointers.
///
/// A fat tagged pointer `p` is a pointer to some storage of type `P` followed by additional storage
/// of type `T`, which can be accessed by offsetting `p`.
template <typename P, typename T>
struct TaggedPointer {

  using data_t = P;
  using tag_t = T;

  /// Returns the offset in bytes of the tag from the base address of a tagged pointer's storage.
  static constexpr std::size_t tag_offset() {
    auto a = alignof(T);
    auto s = sizeof(P);
    return s + (a - s) % a;
  }

  /// Returns a pointer to an instance of `P` tagged with `tag`, calling `initialize` on a pointer
  /// to that tag to initialize the tagged pointee.
  template <typename F>
  requires std::invocable<F, T*>
  static P* pointer(T&& tag, F&& initialize) {
    char* storage = static_cast<char*>(malloc(tag_offset() + sizeof(T)));
    T* h = new(storage + tag_offset()) T(tag);
    return new(storage) P(initialize(h));
  }

  /// Returns a pointer to the tag associated with tagged pointer `p`.
  static T* tag(P* p) {
    char* storage = static_cast<char*>(static_cast<void*>(p));
    return static_cast<T*>(static_cast<void*>(storage + tag_offset()));
  }

  /// Frees the given tagged pointer, destoying its payload before its tag.
  static void free(P* p) {
    p->~P();
    tag(p)->~T();
    ::free(static_cast<void*>(p));
  }

};

}

// SCALALLVM_TAGGED_POINTER_HH
#endif
