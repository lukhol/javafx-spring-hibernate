package com.lukhol.chat.impl;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.lukhol.chat.Settings;
import com.lukhol.chat.URLs;

@Component
public class ClientFactory {
	
	@Autowired
	Settings settings;
	
	public <T> T createServiceImplementation(Class<T> serviceClass){
		switch(settings.getProtocol()) {
			case BURLAP:
				return burlap(serviceClass);
			case HESSIAN:
				return hessian(serviceClass);
			case XMLRPC:
				return xmlrpc(serviceClass);
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
	
	private <T> T xmlrpc(Class<T> serviceClass) { return null; }
	
}
