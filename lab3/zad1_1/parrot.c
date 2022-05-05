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

size_t size(void) {
	return sizeof(struct Parrot);
}

void construct(void *location, const char *name) {
	struct Parrot *parrot = location;
	parrot->vtable = vtable;
	parrot->name = name;
}

void *create(const char *name) {
	struct Parrot *parrot = malloc(size());
	if (parrot == NULL)
		return NULL;

	construct(parrot, name);
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
