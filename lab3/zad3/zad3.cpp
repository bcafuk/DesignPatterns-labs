#include <iostream>
#include <source_location>
#include <memory>

struct Test {
	Test() {
		std::cout << "\tDefault constructor" << std::endl;
	}

	Test(const Test &) {
		std::cout << "\tCopy constructor" << std::endl;
	}

	Test(Test &&) {
		std::cout << "\tMove constructor" << std::endl;
	}

	Test &operator=(const Test &) {
		std::cout << "\tCopy assignment" << std::endl;
		return *this;
	}

	Test &operator=(Test &&) {
		std::cout << "\tMove assignment" << std::endl;
		return *this;
	}

	~Test() {
		std::cout << "\tDestructor" << std::endl;
	}
};

void printLine(const std::source_location location = std::source_location::current()) {
	std::cout << "Line " << location.line() << std::endl;
}

std::unique_ptr<Test> uniqueFunction() {
	printLine();
	return std::make_unique<Test>();
}

void uniqueDemo() {
	printLine();

	{
		printLine();
		std::make_unique<Test>();
		printLine();
	}

	{
		printLine();
		std::unique_ptr<Test> test = std::make_unique<Test>();
		printLine();
	}

	printLine();

	{
		printLine();
		std::unique_ptr<Test> test = uniqueFunction();
		printLine();
	}

	printLine();

	{
		printLine();
		std::unique_ptr<Test> test;
		printLine();
		test = std::make_unique<Test>();
		printLine();
		test = nullptr;
		printLine();
	}

	printLine();
}

std::shared_ptr<Test> sharedFunction() {
	printLine();
	return std::make_shared<Test>();
}

void sharedDemo() {
	printLine();

	{
		printLine();
		std::make_shared<Test>();
		printLine();
	}

	{
		printLine();
		std::shared_ptr<Test> test = std::make_shared<Test>();
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test = sharedFunction();
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test;
		printLine();
		test = std::make_shared<Test>();
		printLine();
		test = nullptr;
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test1 = std::make_shared<Test>();
		printLine();
		std::shared_ptr<Test> test2 = test1;
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test2;
		printLine();
		{
			printLine();
			std::shared_ptr<Test> test1 = std::make_shared<Test>();
			printLine();
			test2 = test1;
			printLine();
		}
		printLine();
	}

	printLine();
}

void weakDemo() {
	printLine();

	{
		printLine();
		std::weak_ptr<Test> weak;
		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();

	{
		printLine();
		std::weak_ptr<Test> weak = std::make_shared<Test>();
		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test = std::make_shared<Test>();
		printLine();
		std::weak_ptr<Test> weak = test;
		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();

	{
		printLine();
		std::weak_ptr<Test> weak;
		printLine();
		{
			printLine();
			std::shared_ptr<Test> test = std::make_shared<Test>();
			printLine();
			weak = test;
			printLine();
		}
		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();

	{
		printLine();
		std::shared_ptr<Test> test1;
		printLine();
		std::weak_ptr<Test> weak;
		printLine();
		{
			printLine();
			std::shared_ptr<Test> test2 = std::make_shared<Test>();
			printLine();
			weak = test2;
			printLine();
			test1 = test2;
			printLine();
		}
		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();

	{
		printLine();
		std::weak_ptr<Test> weak;
		printLine();
		std::shared_ptr<Test> test2;
		printLine();

		{
			printLine();
			std::shared_ptr<Test> test1 = std::make_shared<Test>();
			printLine();
			weak = test1;
			printLine();
			test2 = weak.lock();
			printLine();
		}

		printLine();
		std::cout << "\tExpired: " << weak.expired() << std::endl;
		printLine();
	}

	printLine();
}

int main() {
	uniqueDemo();
	sharedDemo();
	weakDemo();
}
