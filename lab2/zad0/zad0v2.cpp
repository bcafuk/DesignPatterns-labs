#include <iostream>
#include <list>

struct Point {
	int x;
	int y;

	friend std::ostream &operator<<(std::ostream &os, const Point &p) {
		return os << "(" << p.x << ", " << p.y << ")";
	}
};

class Shape {
public:
	virtual void draw() const = 0;
	virtual void move(int dx, int dy) = 0;
};

class Circle : public Shape {
public:
	Circle(const Point &center, double radius) :
			radius(radius),
			center(center) {};

	void draw() const override {
		std::cerr << "in Circle::draw at " << center << "\n";
	}

	void move(int dx, int dy) override {
		center.x += dx;
		center.y += dy;
	}

private:
	double radius;
	Point center;
};

class Square : public Shape {
public:
	Square(const Point &center, double side) :
			side(side),
			center(center) {}

	void draw() const override {
		std::cerr << "in Square::draw at " << center << "\n";
	}

	void move(int dx, int dy) override {
		center.x += dx;
		center.y += dy;
	}

private:
	double side;
	Point center;
};

class Rhombus : public Shape {
public:
	Rhombus(const Point &center, double side, double angle) :
			side(side),
			center(center),
			angle(angle) {}

	void draw() const override {
		std::cerr << "in Rhombus::draw at " << center << "\n";
	}

	void move(int dx, int dy) override {
		center.x += dx;
		center.y += dy;
	}

private:
	double side;
	Point center;
	double angle;
};

void drawShapes(const std::list<Shape *> &shapes) {
	for (const Shape *shape: shapes) {
		shape->draw();
	}
}

void moveShapes(std::list<Shape *> &shapes, int dx, int dy) {
	for (Shape *shape: shapes) {
		shape->move(dx, dy);
	}
}

int main() {
	std::list<Shape *> shapes;

	shapes.push_back(new Circle({0, 0}, 1.0));
	shapes.push_back(new Square({0, 0}, 1.0));
	shapes.push_back(new Rhombus({0, 0}, 1.0, 45.0));
	shapes.push_back(new Circle({0, 0}, 1.0));
	shapes.push_back(new Square({0, 0}, 1.0));
	shapes.push_back(new Rhombus({0, 0}, 1.0, 45.0));

	moveShapes(shapes, 7, 11);

	drawShapes(shapes);
}
