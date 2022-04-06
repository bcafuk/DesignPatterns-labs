#include <stddef.h>
#include <stdio.h>
#include <string.h>

#define ARR_LEN(a) (sizeof(a) / sizeof(*(a)))

const void *mymax(
		const void *base, size_t nmemb, size_t size,
		int (*compar)(const void *, const void *)) {
	const void *max = base;

	const unsigned char *current = base;
	for (int i = 1; i < nmemb; i++) {
		current += size;

		if (compar(current, max)) {
			max = current;
		}
	}

	return max;
}

int gt_int(const void *a, const void *b) {
	return *(const int *) a > *(const int *) b;
}

int gt_char(const void *a, const void *b) {
	return *(const char *) a > *(const char *) b;
}

int gt_str(const void *a, const void *b) {
	return strcmp(*(const char *const *) a, *(const char *const *) b) > 0;
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
char arr_char[] = "Suncana strana ulice";
const char *arr_str[] = {
		"Gle", "malu", "vocku", "poslije", "kise",
		"Puna", "je", "kapi", "pa", "ih", "njise"
};

int main() {
	const int *max_int = mymax(arr_int, ARR_LEN(arr_int), sizeof(int), gt_int);
	printf("%d\n", *max_int);

	const char *max_char = mymax(arr_char, strlen(arr_char), sizeof(char), gt_char);
	printf("%c\n", *max_char);

	const char *const *max_str = mymax(arr_str, ARR_LEN(arr_str), sizeof(const char *), gt_str);
	printf("%s\n", *max_str);
}
