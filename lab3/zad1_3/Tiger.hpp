#ifndef OOUP_TIGER_HPP
#define OOUP_TIGER_HPP

#include <string>

#include "Animal.hpp"

class Tiger : public Animal{
public:
	explicit Tiger(std::string name);

	const char *name() override;
	const char *greet() override;
	const char *menu() override;

private:
	static int hreg;

	const std::string m_name;
};

#endif //OOUP_TIGER_HPP
