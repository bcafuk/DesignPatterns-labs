#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

struct Unary_Function;
typedef double (*virtual_method)(struct Unary_Function *this, double x);

struct Unary_Function {
	const virtual_method *vptr;
	int lower_bound;
	int upper_bound;
};

double Unary_Function_negative_value_at(struct Unary_Function *this, double x);
const virtual_method Unary_Function_vtable[2] = {
		NULL,
		Unary_Function_negative_value_at,
};

void Unary_Function_constructor(struct Unary_Function *this, int lb, int ub) {
	this->vptr = Unary_Function_vtable;
	this->lower_bound = lb;
	this->upper_bound = ub;
}

double Unary_Function_negative_value_at(struct Unary_Function *this, double x) {
	return -this->vptr[0](this, x);
}

void Unary_Function_tabulate(struct Unary_Function *this) {
	for (int x = this->lower_bound; x <= this->upper_bound; x++) {
		printf("f(%d)=%lf\n", x, this->vptr[0](this, x));
	}
}

bool Unary_Function_same_functions_for_ints(struct Unary_Function *f1, struct Unary_Function *f2, double tolerance) {
	if (f1->lower_bound != f2->lower_bound) return false;
	if (f1->upper_bound != f2->upper_bound) return false;
	for (int x = f1->lower_bound; x <= f1->upper_bound; x++) {
		double delta = f1->vptr[0](f1, x) - f2->vptr[0](f2, x);
		if (delta < 0) delta = -delta;
		if (delta > tolerance) return false;
	}
	return true;
}

struct Square {
	struct Unary_Function base;
};

double Square_value_at(struct Unary_Function *this, double x);
const virtual_method Square_vtable[2] = {
		Square_value_at,
		Unary_Function_negative_value_at,
};

void Square_constructor(struct Square *this, int lb, int ub) {
	Unary_Function_constructor(&this->base, lb, ub);
	this->base.vptr = Square_vtable;
}

double Square_value_at(struct Unary_Function *this, double x) {
	return x * x;
}

struct Linear {
	struct Unary_Function base;
	double a;
	double b;
};

double Linear_value_at(struct Unary_Function *this, double x);
const virtual_method Linear_vtable[2] = {
		Linear_value_at,
		Unary_Function_negative_value_at,
};

void Linear_constructor(struct Linear *this, int lb, int ub, double a_coef, double b_coef) {
	Unary_Function_constructor(&this->base, lb, ub);
	this->base.vptr = Linear_vtable;
	this->a = a_coef;
	this->b = b_coef;
}

double Linear_value_at(struct Unary_Function *this, double x) {
	return ((struct Linear *) this)->a * x + ((struct Linear *) this)->b;
}

int main(void) {
	struct Unary_Function *f1 = malloc(sizeof(struct Square));
	Square_constructor((struct Square *) f1, -2, 2);
	Unary_Function_tabulate(f1);

	struct Unary_Function *f2 = malloc(sizeof(struct Linear));
	Linear_constructor((struct Linear *) f2, -2, 2, 5, -2);
	Unary_Function_tabulate(f2);

	printf("f1==f2: %s\n", Unary_Function_same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
	printf("neg_val f2(1) = %lf\n", Unary_Function_negative_value_at(f2, 1.0));

	free(f1);
	free(f2);
	return 0;
}
