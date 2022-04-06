#include <iostream>
#include <cassert>
#include <cstdlib>

struct Point {
	int x;
	int y;

	friend std::ostream &operator<<(std::ostream &os, const Point &p) {
		return os << "(" << p.x << ", " << p.y << ")";
	}
};

struct Shape {
	enum EType {
		circle, square, rhombus
	};
	EType type_;
};

struct Circle {
	Shape::EType type_;
	double radius_;
	Point center_;
};

struct Square {
	Shape::EType type_;
	double side_;
	Point center_;
};

struct Rhombus {
	Shape::EType type_;
	double side_;
	Point center_;
	double angle_;
};

void drawSquare(struct Square *s) {
	std::cerr << "in drawSquare at " << s->center_ << "\n";
}

void drawCircle(struct Circle *c) {
	std::cerr << "in drawCircle at" << c->center_ << "\n";
}

void drawRhombus(struct Rhombus *r) {
	std::cerr << "in drawRhombus at" << r->center_ << "\n";
}

void drawShapes(Shape **shapes, int n) {
	for (int i = 0; i < n; ++i) {
		struct Shape *s = shapes[i];
		switch (s->type_) {
			case Shape::square:
				drawSquare((struct Square *) s);
				break;
			case Shape::circle:
				drawCircle((struct Circle *) s);
				break;
			case Shape::rhombus:
				drawRhombus((struct Rhombus *) s);
				break;
			default:
				assert(0);
				exit(0);
		}
	}
}

void moveShapes(Shape **shapes, int n, int dx, int dy) {
	for (int i = 0; i < n; ++i) {
		struct Shape *s = shapes[i];
		switch (s->type_) {
			case Shape::square:
				((struct Square *) s)->center_.x += dx;
				((struct Square *) s)->center_.y += dy;
				break;
			case Shape::circle:
				((struct Circle *) s)->center_.x += dx;
				((struct Circle *) s)->center_.y += dy;
				break;
			case Shape::rhombus:
				((struct Rhombus *) s)->center_.x += dx;
				((struct Rhombus *) s)->center_.y += dy;
				break;
			default:
				assert(0);
				exit(0);
		}
	}
}

int main() {
	Shape *shapes[6];

	shapes[0] = (Shape *) new Circle;
	shapes[0]->type_ = Shape::circle;

	shapes[1] = (Shape *) new Square;
	shapes[1]->type_ = Shape::square;

	shapes[2] = (Shape *) new Rhombus;
	shapes[2]->type_ = Shape::rhombus;

	shapes[3] = (Shape *) new Square;
	shapes[3]->type_ = Shape::square;

	shapes[4] = (Shape *) new Circle;
	shapes[4]->type_ = Shape::circle;

	shapes[5] = (Shape *) new Rhombus;
	shapes[5]->type_ = Shape::rhombus;

	moveShapes(shapes, 6, 7, 11);

	drawShapes(shapes, 6);
}
