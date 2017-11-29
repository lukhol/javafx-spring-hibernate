package com.lukhol.chat.fxcontrolls;

import com.lukhol.chat.models.User;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class UserListCell  extends ListCell<User> {
	
	private static int count = 0;
	
//	HBox hbox = new HBox();
//	Label label = new Label();
	
	@Override
	public void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item != null) {
			
			HBox hbox = new HBox();
			Label label = new Label();
			
			if(count%2 == 0) {

			}
			
			label.setText(item.getUsername());
			label.setStyle("-fx-background-color: #434343; -fx-padding: 10px; -fx-background-radius: 10px; -fx-text-fill: white;");
			
			hbox.getChildren().add(label);
            setGraphic(hbox);
            count++;
		}
	}
}
