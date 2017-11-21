package com.lukhol.politechnika;
	
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

//@Service
public class Main extends Application {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	//private static ApplicationContext springContext = new AnnotationConfigApplicationContext(AppConfig.class);
	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
	
	@Override
	public void start(Stage primaryStage) {
		try {
			logger.info("Application start.");
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
