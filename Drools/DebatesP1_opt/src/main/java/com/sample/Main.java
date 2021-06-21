package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class Main {

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-rules");

			// Cargar personas que debaten
			// nombre - jugador - edad - credibilidad - ataque
			Debater d01 = new Debater("Mike",  0, 18, 30, 18);
			Debater d02 = new Debater("Liu",   0, 20, 40, 15);
			Debater d03 = new Debater("Simon", 0, 21, 20, 20);
			kSession.insert(d01);
			kSession.insert(d02);
			kSession.insert(d03);

			Debater d11 = new Debater("Samantha", 1, 25, 50, 25);
			Debater d12 = new Debater("Ryan",     1, 19, 20, 12);
			Debater d13 = new Debater("Mary",     1, 23, 20, 16);
			kSession.insert(d11);
			kSession.insert(d12);
			kSession.insert(d13);
			
			// Disparar las reglas
			kSession.fireAllRules();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}