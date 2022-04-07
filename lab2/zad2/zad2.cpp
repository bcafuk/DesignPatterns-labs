#include <cstring>
#include <iostream>
#include <vector>
#include <set>

#define ARR_LEN(a) (sizeof(a) / sizeof(*(a)))

template<typename Iterator, typename Predicate>
Iterator mymax(
		Iterator first, Iterator last, Predicate pred) {
	Iterator max = first;

	/*
	 * The passed iterator is incremented directly instead of being duplicated
	 * because there exist iterators which are not multi-pass.
	 * Iterators which are not multi-pass do not support copying. Therefore,
	 * choosing not to copy the iterator allows this function to work for both
	 * - input iterators (which are not multi-pass),
	 * - and forward iterators (which are multi-pass).
	 */
	for (first++; first != last; first++) {
		if (pred(*first, *max)) {
			max = first;
		}
	}

	return max;
}

bool gt_int(const int &a, const int &b) {
	return a > b;
}

bool gt_char(const char &a, const char &b) {
	return a > b;
}

bool gt_str(const char *const &a, const char *const &b) {
	return std::strcmp(a, b) > 0;
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
char arr_char[] = "Suncana strana ulice";
const char *arr_str[] = {
		"Gle", "malu", "vocku", "poslije", "kise",
		"Puna", "je", "kapi", "pa", "ih", "njise"
};

std::vector<int> vec_int = {1, 3, 5, 7, 4, 6, 9, 2, 0};
std::set<int> set_int = {1, 3, 5, 7, 4, 6, 9, 2, 0};

int main() {
	std::cout << *mymax(&arr_int[0], &arr_int[ARR_LEN(arr_int)], gt_int) << "\n";

	std::cout << *mymax(&arr_char[0], &arr_char[std::strlen(arr_char)], gt_char) << "\n";

	std::cout << *mymax(&arr_str[0], &arr_str[ARR_LEN(arr_str)], gt_str) << "\n";

	std::cout << *mymax(vec_int.begin(), vec_int.end(), gt_int) << "\n";

	std::cout << *mymax(set_int.begin(), set_int.end(), gt_int) << "\n";
}
