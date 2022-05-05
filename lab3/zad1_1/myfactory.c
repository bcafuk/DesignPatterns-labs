#include "myfactory.h"

#include <dlfcn.h>
#include <limits.h>
#include <stddef.h>
#include <stdio.h>

static char libpath[PATH_MAX];

void *myfactory(char const *libname, char const *ctorarg) {
	int requiredLength = snprintf(libpath, PATH_MAX, "./%s.so", libname);
	if (requiredLength < 0 || requiredLength >= sizeof libpath)
		return NULL;

	void *libHandle = dlopen(libpath, RTLD_LAZY);
	void *(*create)(const char *) = dlsym(libHandle, "create");

	if (create == NULL)
		return NULL;
	return create(ctorarg);
}
