package com.lukhol.chat.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.lukhol.chat.Settings;
import com.lukhol.chat.URLs;
import com.lukhol.chat.services.ChatService;
import com.lukhol.chat.services.ChatServiceWrapper;

@Component
public class MyClientFactory {
	
	@Autowired
	Settings settings;
	
	public <T> T createServiceImplementation(Class<T> serviceClass){
		switch(settings.getProtocol()) {
			case BURLAP:
				return burlap(serviceClass);
			case HESSIAN:
				return hessian(serviceClass);
			case XMLRPC:
				return (T)xmlrpc();
			default:
				return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T burlap(Class<T> serviceClass) {
		String url = URLs.MAIN_URL + "burlap" + serviceClass.getSimpleName().replaceAll("Service", "");

	    BurlapProxyFactory factory = new BurlapProxyFactory();
		T service = null;
		
		try {
			service = (T) factory.create(serviceClass, url);
			//System.out.println("BurlapClient created.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return service;
	}
	
	
	@SuppressWarnings("unchecked")
	private <T> T hessian(Class<T> serviceClass){
		String url = URLs.MAIN_URL + "hessian" + serviceClass.getSimpleName().replaceAll("Service", "");
		
		HessianProxyFactory factory = new HessianProxyFactory();
		T service = null;
		try {
			service = (T) factory.create(serviceClass, url);
			//System.out.println("HessianClient created.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return service;
	}
	
	private ChatService xmlrpc() { 
		String url = URLs.MAIN_URL + "xmlRpc";
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(url));
			config.setEnabledForExtensions(true);

			XmlRpcClient client = new XmlRpcClient();

			// use Commons HttpClient as transport
			client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
			client.setConfig(config);

			return new ChatServiceWrapper(client);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
