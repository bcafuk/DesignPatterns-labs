#include <cstdio>

class CoolClass {
public:
	virtual void set(int x) { x_ = x; };

	virtual int get() { return x_; };
private:
	int x_;
};

class PlainOldClass {
public:
	void set(int x) { x_ = x; };

	int get() { return x_; };
private:
	int x_;
};

int main() {
	std::printf("Size of function pointer: %zu\n", sizeof(void (*)()));
	std::printf("Alignment of function pointer: %zu\n", alignof(void (*)()));

	std::printf("Size of CoolClass: %zu\n", sizeof(CoolClass));
	std::printf("Size of PlainOldClass: %zu\n", sizeof(PlainOldClass));
}
