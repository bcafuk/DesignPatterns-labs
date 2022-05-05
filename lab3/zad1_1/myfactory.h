#ifndef OOUP_MYFACTORY_H
#define OOUP_MYFACTORY_H

#include <stddef.h>

struct AnimalAllocator {
	void *(*create)(const char *);
	size_t (*size)(void);
	void (*construct)(void *, const char *);
};

struct AnimalAllocator myfactory(char const *libname);

#endif //OOUP_MYFACTORY_H
