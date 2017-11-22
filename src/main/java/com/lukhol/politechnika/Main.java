package com.lukhol.politechnika;
	
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	public static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
	private static Stage primaryStage;
	
	@Override
	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		
		try {		
			logger.info("Application start.");
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/LoginWindow.fxml"));
			loader.setControllerFactory(context::getBean);
			
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("First Window.");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void changeScene(URL sceneResourceUrl) {
		changeScene(sceneResourceUrl, null);
	}
	
	public static void changeScene(URL sceneResourceUrl, String title) {
		
		if(title != null)
			primaryStage.setTitle(title);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(sceneResourceUrl);
		loader.setControllerFactory(Main.context::getBean);
		
		Parent root = null;
		try {
			root = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		primaryStage.setScene(new Scene(root));
	}
}
