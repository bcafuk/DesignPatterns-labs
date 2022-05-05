#include <stdlib.h>

typedef const char *(*PTRFUN)();

struct Parrot {
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
	struct Parrot *parrot = malloc(sizeof(struct Parrot));
	if (parrot == NULL)
		return NULL;

	parrot->vtable = vtable;
	parrot->name = name;

	return parrot;
}

char const *name(void *this) {
	return ((const struct Parrot *) this)->name;
}

char const *greet() {
	return "pjev-pjev";
}

char const *menu() {
	return "sjemenke";
}
