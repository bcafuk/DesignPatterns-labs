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

void *create(char const *name) {
	struct Tiger *tiger = malloc(sizeof(struct Tiger));
	if (tiger == NULL)
		return NULL;

	tiger->vtable = vtable;
	tiger->name = name;

	return tiger;
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
