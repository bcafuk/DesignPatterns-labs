#ifndef OOUP_MYFACTORY_HPP
#define OOUP_MYFACTORY_HPP

#include <map>
#include <string>
#include <vector>

class myfactory {
public:
	typedef void *(*pFunCreator)(const std::string &);
	typedef std::map<std::string, pFunCreator> MyMap;

	static myfactory &instance();

	myfactory(const myfactory &) = delete;
	myfactory &operator=(const myfactory &) = delete;

	int registerCreator(const std::string &id, pFunCreator pfn);

	void *create(const std::string &id, const std::string &arg);
	std::vector<std::string> getIds();

private:
	myfactory() = default;
	~myfactory() = default;

	MyMap creators_;
};


#endif //OOUP_MYFACTORY_HPP
