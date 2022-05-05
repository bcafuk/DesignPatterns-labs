#include "Parrot.hpp"

#include <utility>

#include "myfactory.hpp"

int Parrot::hreg = myfactory::instance().registerCreator("parrot", [](const std::string &name) {
	return (void *) new Parrot(name);
});

Parrot::Parrot(std::string name)
		: m_name(std::move(name)) {}

const char *Parrot::name() {
	return m_name.c_str();
}

const char *Parrot::greet() {
	return "pjev-pjev";
}

const char *Parrot::menu() {
	return "sjemenke";
}
