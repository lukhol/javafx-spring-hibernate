package com.lukhol.chat;
	
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lukhol.chat.impl.ClientFactory;
import com.lukhol.chat.services.ChatService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
	public static final Map<PageName, String> fxmlPages = new HashMap<>();
	private static Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		initMap();
		
		try {		
			changeScene(PageName.LoginPage, "Login");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured and stop method will be executed. ");
			stop();
		}
	}
	
	@Override
	public void stop() {
		Settings settings = context.getBean(Settings.class);
		
		ClientFactory clientFactory = context.getBean(ClientFactory.class);
		ChatService chatService = clientFactory.createServiceImplementation(ChatService.class);
		
		if(settings.getLoggedInUser() != null)
			chatService.logout(settings.getLoggedInUser());
		
		for(Thread thread : settings.getThreads()) {
			thread.stop();
		}
		
		System.out.println("Succesfully exited.");
	}
	
	public static void changeScene(PageName pageName) {
		changeScene(pageName, null);
	}
	
	public static void changeScene(PageName pageName, String title) {
		
		if(title != null)
			primaryStage.setTitle(title);
		
		if(!fxmlPages.containsKey(pageName))
			return;
		
		URL sceneResourceUrl = Main.class.getResource(fxmlPages.get(pageName));
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(sceneResourceUrl);
		loader.setControllerFactory(Main.context::getBean);
		
		Parent root = null;
		try {
			root = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Main.class.getResource("/css/app.css").toExternalForm());
		primaryStage.setScene(scene);
	}
	
	private final void initMap() {
		fxmlPages.put(PageName.LoginPage, "/fxml/LoginWindow.fxml");
		fxmlPages.put(PageName.RegisterPage, "/fxml/RegistrationWindow.fxml");
		fxmlPages.put(PageName.ChatPage, "/fxml/ChatWindow.fxml");
	}
}
