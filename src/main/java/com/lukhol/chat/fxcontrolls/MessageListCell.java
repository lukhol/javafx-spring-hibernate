package com.lukhol.chat.fxcontrolls;

import com.lukhol.chat.models.fx.MessageFX;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MessageListCell extends ListCell<MessageFX> {
	
	public MessageListCell(ListView<MessageFX> parentListView) {
		this.parentListView = parentListView;
	}
	
	private ListView<MessageFX> parentListView;
	HBox hbox;
	Label label;
	
	@Override
	public void updateItem(MessageFX item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item != null) {
			hbox = new HBox();
			label = new Label();
			label.setWrapText(true);
			label.setMaxWidth(parentListView.getWidth() - 60);
			
			parentListView.widthProperty().addListener((obs, oldVal, newVal) -> {
			     double newWidth = (double)newVal;
			     label.setMaxWidth(newWidth - 60);
			});
			
			if(item.isClientOwner()) {
				Region region = new Region();
				HBox.setHgrow(region, Priority.ALWAYS);
				hbox.getChildren().add(region);
				
				HBox.setMargin(label,  new Insets(2, 2, 2, 25));
				label.setStyle("-fx-background-color: green; -fx-padding: 10px; -fx-background-radius: 10px; -fx-text-fill: white;");
			} else {
				HBox.setMargin(label,  new Insets(2, 25, 2, 2));
				label.setStyle("-fx-background-color: coral; -fx-padding: 10px; -fx-background-radius: 10px; -fx-text-fill: white;");
			}
			
			label.setText(item.getMessage().getMessageContent());
			
			hbox.getChildren().add(label);	
            setGraphic(hbox);
            
            parentListView.scrollTo(parentListView.getItems().get(parentListView.getItems().size() - 1));
		} else {
			setGraphic(null);
		}
	}
}