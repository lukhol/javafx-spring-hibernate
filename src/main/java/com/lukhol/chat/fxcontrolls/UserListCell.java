package com.lukhol.chat.fxcontrolls;

import com.lukhol.chat.models.User;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class UserListCell extends ListCell<User> {		
	public String test;
		
	HBox hbox;
	Label label;
	
	@Override
	public void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		hbox = new HBox();
		label = new Label();
		
		if(item != null) {						
			label.setText(item.getUsername());
			label.setStyle("-fx-background-color: #434343; -fx-padding: 5px; -fx-background-radius: 4px; -fx-text-fill: white;");
			hbox.getChildren().add(label);
            setGraphic(hbox);
		} else {
			setGraphic(null);
		}
	}
}
