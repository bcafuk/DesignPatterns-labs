#include "Tiger.hpp"

#include <utility>

#include "myfactory.hpp"

int Tiger::hreg = myfactory::instance().registerCreator("tiger", [](const std::string &name) {
	return (void *) new Tiger(name);
});

Tiger::Tiger(std::string name)
		: m_name(std::move(name)) {}

const char *Tiger::name() {
	return m_name.c_str();
}

const char *Tiger::greet() {
	return "grrrr";
}

const char *Tiger::menu() {
	return "jelene";
}
