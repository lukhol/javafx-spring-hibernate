package com.lukhol.politechnika.impl;

import java.net.MalformedURLException;

import org.springframework.stereotype.Component;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.lukhol.politechnika.URLs;

@Component
public class ClientFactory {
	
	@SuppressWarnings("unchecked")
	public <T> T burlap(Class<T> serviceClass) {
		String url = URLs.MAIN_URL + "burlap" + serviceClass.getSimpleName().replaceAll("Service", "");

	    BurlapProxyFactory factory = new BurlapProxyFactory();
		T service = null;
		
		try {
			service = (T) factory.create(serviceClass, url);
			System.out.println("BurlapClient created.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return service;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T hessian(Class<T> serviceClass){
		String url = URLs.MAIN_URL + "hessian" + serviceClass.getSimpleName().replaceAll("Service", "");
		
		HessianProxyFactory factory = new HessianProxyFactory();
		T service = null;
		try {
			service = (T) factory.create(serviceClass, url);
			System.out.println("HessianClient created.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return service;
	}
}
