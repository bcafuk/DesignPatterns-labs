package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.Renderer;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;

import java.util.*;

public final class CompositeShape extends AbstractGraphicalObject {
    private final List<GraphicalObject> children;

    public CompositeShape() {
        this(List.of());
    }

    public CompositeShape(List<GraphicalObject> children) {
        super(new Point[0]);

        this.children = new ArrayList<>(children);
        for (GraphicalObject child : children)
            child.addGraphicalObjectListener(new GraphicalObjectListener() {
                @Override
                public void graphicalObjectChanged(GraphicalObject go) {
                    notifyListeners();
                }

                @Override
                public void graphicalObjectSelectionChanged(GraphicalObject go) {}
            });
    }

    public List<GraphicalObject> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Rectangle getBoundingBox() {
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;

        for (GraphicalObject child : children) {
            Rectangle boundingBox = child.getBoundingBox();
            left = Math.min(left, boundingBox.left());
            top = Math.min(top, boundingBox.top());
            right = Math.max(right, boundingBox.right());
            bottom = Math.max(bottom, boundingBox.bottom());
        }

        return new Rectangle(left, top, right - left, bottom - top);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return children.stream()
                       .map(go -> go.selectionDistance(mousePoint))
                       .reduce(Math::min)
                       .orElse(Double.POSITIVE_INFINITY);
    }

    @Override
    public void translate(Point delta) {
        for (GraphicalObject child : children)
            child.translate(delta);
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject child : children)
            child.render(r);
    }

    @Override
    public String getShapeName() {
        return "Grupa";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> clonedChildren = children.stream()
                                                       .map(GraphicalObject::duplicate)
                                                       .toList();
        return new CompositeShape(clonedChildren);
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] args = data.split("\\s+");
        if (args.length != 1)
            throw new IllegalArgumentException("Expected 1 argument, but got " + args.length);

        int numChildren = Integer.parseInt(args[0]);

        List<GraphicalObject> children = new LinkedList<>();
        for (int i = 0; i < numChildren; i++)
            children.add(0, stack.pop());

        stack.push(new CompositeShape(children));
    }

    @Override
    public void save(List<String> rows) {
        for (GraphicalObject child : children)
            child.save(rows);
        rows.add(String.format("%s %d", getShapeID(), children.size()));
    }
}
