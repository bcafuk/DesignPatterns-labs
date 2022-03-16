#include <iostream>

class B {
public:
	virtual int prva() = 0;
	virtual int druga(int) = 0;
};

class D : public B {
public:
	virtual int prva() { return 42; }

	virtual int druga(int x) { return prva() + x; }
};

void fun(B *pb) {
	std::cout << (*(int (***)(B *)) pb)[0](pb) << std::endl;
	std::cout << (*(int (***)(B *, int)) pb)[1](pb, 1295) << std::endl;
}

int main() {
	D d;
	fun(&d);
}
