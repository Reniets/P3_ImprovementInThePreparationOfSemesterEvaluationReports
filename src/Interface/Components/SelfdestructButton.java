package Interface.Components;

import Exceptions.NotInPaneException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;


// A button to be associated with an object and a list of collections it might be in.
// Also associated with a Pane (preferably containing itself)
// When pressed, removes the target object from the given collections, and removes its associated Pane from the GUI.
public class SelfdestructButton extends Button {

    // Fields:
    private Object target;
    private Collection location;
    private Pane buttonLocationBox;
    private Pane buttonLocationBoxParentPane;

    //////////////////
    // Constructors //
    //////////////////

    // Target is the object to be removed
    // Location is the collection to remove() from
    // buttonLocationBox is the Pane to be removed

    public SelfdestructButton(Object target, Collection location, Pane buttonLocationBox) throws NotInPaneException {

        // If the buttonLocationBox is properly in a Pane, we grab that Pane, otherwise throw an exception
        if (buttonLocationBox.getParent() instanceof Pane) {
            buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }

        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this);
    }

    public SelfdestructButton(String text, Object target, Collection location, Pane buttonLocationBox) throws NotInPaneException {
        super(text);
        // Safe cast
        if (buttonLocationBox.getParent() instanceof Pane) {
            buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }
        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this);
    }

    public SelfdestructButton(String text, Node graphic, Object target, Collection location, Pane buttonLocationBox) throws NotInPaneException {
        super(text, graphic);
        // Safe cast
        if (buttonLocationBox.getParent() instanceof Pane) {
            buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }
        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this);
    }

    // Variants with extra button functionality

    public SelfdestructButton(Object target, Collection location, Pane buttonLocationBox, Method functionality, Object caller) throws NotInPaneException {

        // If the buttonLocationBox is properly in a Pane, we grab that Pane, otherwise throw an exception
        if (buttonLocationBox.getParent() instanceof Pane) {
            buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }

        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this, functionality, caller);
    }

    public SelfdestructButton(String text, Object target, Collection location, Pane buttonLocationBox, Method functionality, Object caller) throws NotInPaneException {
        super(text);
        // Safe cast
        if (buttonLocationBox.getParent() instanceof Pane) {
            buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }
        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this, functionality, caller);
    }

    public SelfdestructButton(String text, Node graphic, Object target, Collection location, Pane buttonLocationBox, Method functionality, Object caller) throws NotInPaneException {
        super(text, graphic);
        // Safe cast
        if (buttonLocationBox.getParent() instanceof Pane) {
            this.buttonLocationBoxParentPane = (Pane) buttonLocationBox.getParent();
        } else {
            throw new NotInPaneException("SelfdestructButton improperly contained.");
        }
        this.target = target;
        this.location = location;
        this.buttonLocationBox = buttonLocationBox;

        primeSelfDestructButton(this, functionality, caller);
    }

    ///////////////////
    // Functionality //
    ///////////////////

    // Functionality of selfdestructbuttons: Removes its parent box, and its target from the collection
    private void primeSelfDestructButton(SelfdestructButton selfdestructButton) {
        selfdestructButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Remove target from collection
                location.remove(target);

                // Delete the box this button is contained within
                buttonLocationBoxParentPane.getChildren().remove(buttonLocationBox);
            }
        });
    }

    // Funtionality of expanded selfdestructbutton: Removes its parent box, and its target from the collection,
    // and then calls the given Method object.
    private void primeSelfDestructButton(SelfdestructButton selfdestructButton, Method functionality, Object caller) {
        selfdestructButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Remove target from collection
                location.remove(target);

                // Delete the box this button is contained within
                buttonLocationBoxParentPane.getChildren().remove(buttonLocationBox);

                // Invoke the given Method
                try {
                    functionality.invoke(caller);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
