package Interface.Components;

import javafx.scene.Node;

import java.util.Objects;

public class GraphicUpdate {

    private Node updateSource;
    private GraphicUpdateListener listener;

    public GraphicUpdate(Node updateSource, GraphicUpdateListener listener) {
        this.updateSource = updateSource;
        this.listener = listener;
    }

    public void call() {
        this.listener.call();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphicUpdate that = (GraphicUpdate) o;
        return updateSource.equals(that.updateSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updateSource);
    }
}
