#include <stdlib.h>

typedef const char *(*PTRFUN)();

struct Tiger {
	PTRFUN *vtable;
	const char *name;
};

static const char *name(void *this);
static const char *greet();
static const char *menu();

static const char *(*vtable[3])() = {
		name,
		greet,
		menu,
};

size_t size(void) {
	return sizeof(struct Tiger);
}

void construct(void *location, const char *name) {
	struct Tiger *tiger = location;
	tiger->vtable = vtable;
	tiger->name = name;
}

void *create(const char *name) {
	struct Tiger *tiger = malloc(size());
	if (tiger == NULL)
		return NULL;

	construct(tiger, name);
}

char const *name(void *this) {
	return ((const struct Tiger *) this)->name;
}

char const *greet() {
	return "grrrrr";
}

char const *menu() {
	return "jelene";
}
