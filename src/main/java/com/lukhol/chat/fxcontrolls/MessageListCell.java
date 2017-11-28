package com.lukhol.chat.fxcontrolls;

import com.lukhol.chat.models.User;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MessageListCell extends ListCell<User> {
	
	private static int count = 0;
	
	HBox hbox = new HBox();
	Label label = new Label();
	Rectangle rect = new Rectangle(50, 50);
	
	@Override
	public void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item != null) {
			
			if(count%2 == 0) {
				Region region = new Region();
				HBox.setHgrow(region, Priority.ALWAYS);
				hbox.getChildren().add(region);
			}
			
			label.setText(item.getUsername());
			label.setStyle("-fx-background-color: coral; -fx-padding: 10px; -fx-background-radius: 10px; -fx-text-fill: white;");
			
			hbox.getChildren().add(label);
			rect.setFill(Color.BLACK);
			
            setGraphic(hbox);
            count++;
		}
	}
}