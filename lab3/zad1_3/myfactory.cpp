#include "myfactory.hpp"

myfactory& myfactory::instance(){
	static myfactory theFactory;
	return theFactory;
}

int myfactory::registerCreator(const std::string &id, myfactory::pFunCreator pfn) {
	creators_[id] = pfn;
}

std::vector<std::string> myfactory::getIds() {
	std::vector<std::string> ids;

	for (auto &entry : creators_)
		ids.push_back(entry.first);

	return ids;
}

void *myfactory::create(const std::string &id, const std::string &arg) {
	return creators_[id](arg);
}
