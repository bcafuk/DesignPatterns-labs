#ifndef OOUP_PARROT_HPP
#define OOUP_PARROT_HPP

#include <string>

#include "Animal.hpp"

class Parrot : public Animal {
public:
	explicit Parrot(std::string name);

	const char *name() override;
	const char *greet() override;
	const char *menu() override;

private:
	static int hreg;

	const std::string m_name;
};


#endif //OOUP_PARROT_HPP
