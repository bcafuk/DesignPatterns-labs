#ifndef OOUP_ANIMAL_HPP
#define OOUP_ANIMAL_HPP

class Animal {
public:
	virtual char const *name() = 0;
	virtual char const *greet() = 0;
	virtual char const *menu() = 0;

	virtual ~Animal() = default;
};

#endif //OOUP_ANIMAL_HPP
