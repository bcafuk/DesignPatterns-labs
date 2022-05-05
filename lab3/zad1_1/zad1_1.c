#include "myfactory.h"

#include <stdio.h>
#include <stdlib.h>

typedef char const *(*PTRFUN)();

struct Animal {
	PTRFUN *vtable;
	// vtable entries:
	// 0: char const* name(void* this);
	// 1: char const* greet();
	// 2: char const* menu();
};

// parrots and tigers defined in respective dynamic libraries

void animalPrintGreeting(const struct Animal *animal);
void animalPrintMenu(const struct Animal *animal);

int main(int argc, char *argv[]) {
	for (int i = 1; i < argc; ++i) {
		struct AnimalAllocator allocator = myfactory(argv[i]);
		struct Animal *p = alloca(allocator.size());
		allocator.construct(p, "Modrobradi");

		if (!p) {
			printf("Creation of plug-in object %s failed.\n", argv[i]);
			continue;
		}

		animalPrintGreeting(p);
		animalPrintMenu(p);
	}
}

void animalPrintGreeting(const struct Animal *animal) {
	printf("%s pozdravlja: %s\n", animal->vtable[0](animal), animal->vtable[1]());
}

void animalPrintMenu(const struct Animal *animal) {
	printf("%s voli %s\n", animal->vtable[0](animal), animal->vtable[2]());
}
