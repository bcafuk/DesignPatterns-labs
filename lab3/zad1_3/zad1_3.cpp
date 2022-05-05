#include <iostream>
#include <vector>
#include <sstream>

#include "myfactory.hpp"
#include "Animal.hpp"

void printGreeting(Animal &animal);
void printMenu(Animal &animal);

int main() {
	myfactory &fact(myfactory::instance());
	std::vector<std::string> vecIds = fact.getIds();
	for (int i = 0; i < vecIds.size(); ++i) {
		std::ostringstream oss;
		oss << "Ljubimac " << i;
		Animal *pa = (Animal *) fact.create(vecIds[i], oss.str());
		printGreeting(*pa);
		printMenu(*pa);
		delete pa;
	}
}

void printGreeting(Animal &animal) {
	std::cout << animal.name() << " pozdravlja: " << animal.greet() << "\n";
}

void printMenu(Animal &animal) {
	std::cout << animal.name() << " voli " << animal.menu() << "\n";
}
