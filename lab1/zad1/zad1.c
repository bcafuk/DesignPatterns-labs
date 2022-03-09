#include <stdio.h>
#include <stdlib.h>

typedef char const *(*PTRFUN)();

struct Animal {
	const PTRFUN *vptr;
	const char *name;
};
void animalPrintGreeting(const struct Animal *animal);
void animalPrintMenu(const struct Animal *animal);

void constructDog(struct Animal *dest, const char *name);
const char *dogGreet(void);
const char *dogMenu(void);
const PTRFUN dogVTable[2] = {
		dogGreet,
		dogMenu,
};

void constructCat(struct Animal *dest, const char *name);
const char *catGreet(void);
const char *catMenu(void);
const PTRFUN catVTable[2] = {
		catGreet,
		catMenu,
};

struct Animal *createDog(const char *name);
struct Animal *createDogs(size_t dogCount);
struct Animal *createCat(const char *name);

void testAnimals(void);

#define EXAMPLE_DOG_COUNT 4

int main(void) {
	testAnimals();

	struct Animal stackCat;
	constructCat(&stackCat, "Stosko");

	struct Animal *heapCat = malloc(sizeof(struct Animal));
	constructCat(heapCat, "Gomilko");

	animalPrintGreeting(&stackCat);
	animalPrintGreeting(heapCat);

	free(heapCat);

	struct Animal *sledDogs = createDogs(EXAMPLE_DOG_COUNT);
	for (size_t i = 0; i < EXAMPLE_DOG_COUNT; i++) {
		animalPrintGreeting(&sledDogs[i]);
	}
	free(sledDogs);
}

void testAnimals(void) {
	struct Animal *p1 = createDog("Hamlet");
	struct Animal *p2 = createCat("Ofelija");
	struct Animal *p3 = createDog("Polonije");

	animalPrintGreeting(p1);
	animalPrintGreeting(p2);
	animalPrintGreeting(p3);

	animalPrintMenu(p1);
	animalPrintMenu(p2);
	animalPrintMenu(p3);

	free(p1);
	free(p2);
	free(p3);
}

struct Animal *createDog(const char *name) {
	struct Animal *dog = malloc(sizeof(struct Animal));
	constructDog(dog, name);

	return dog;
}

struct Animal *createDogs(size_t dogCount) {
	struct Animal *dogs = malloc(dogCount * sizeof(struct Animal));

	for (size_t i = 0; i < dogCount; i++) {
		constructDog(&dogs[i], "Mass-produced dog");
	}

	return dogs;
}

struct Animal *createCat(const char *name) {
	struct Animal *cat = malloc(sizeof(struct Animal));
	constructCat(cat, name);

	return cat;
}

void animalPrintGreeting(const struct Animal *animal) {
	printf("%s pozdravlja: %s\n", animal->name, animal->vptr[0]());
}

void animalPrintMenu(const struct Animal *animal) {
	printf("%s voli %s\n", animal->name, animal->vptr[1]());
}

void constructDog(struct Animal *dest, const char *name) {
	dest->vptr = dogVTable;
	dest->name = name;
}

char const *dogGreet(void) {
	return "vau!";
}

char const *dogMenu(void) {
	return "kuhanu govedinu";
}

void constructCat(struct Animal *dest, const char *name) {
	dest->vptr = catVTable;
	dest->name = name;
}

char const *catGreet(void) {
	return "mijau!";
}

char const *catMenu(void) {
	return "konzerviranu tunjevinu";
}
