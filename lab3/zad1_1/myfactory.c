#include "myfactory.h"

#include <dlfcn.h>
#include <limits.h>
#include <stddef.h>
#include <stdio.h>

static char libpath[PATH_MAX];

struct AnimalAllocator myfactory(char const *libname) {
	struct AnimalAllocator allocator = {NULL, NULL, NULL};

	int requiredLength = snprintf(libpath, PATH_MAX, "./%s.so", libname);
	if (requiredLength < 0 || requiredLength >= sizeof libpath)
		return allocator;

	void *libHandle = dlopen(libpath, RTLD_LAZY);

	allocator.create = dlsym(libHandle, "create");
	allocator.size = dlsym(libHandle, "size");
	allocator.construct = dlsym(libHandle, "construct");
	return allocator;
}
