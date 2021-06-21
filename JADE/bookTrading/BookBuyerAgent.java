/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
 *****************************************************************/

package p6.bookTrading;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.HashMap;


public class BookBuyerAgent extends Agent {
	private String BOOK_TRADING_ID = "book-trade";
	private String PRICE_LIST_ID = "price-books-list";
	// Libro que se quiere comprar
	private String targetBookTitle;
	// Nº de ofertas
	private Integer numPeticionesVendedores;
	// Máximo precio aceptdo
	private Integer precioMax;
	// Precio mínimo de venta por parte de un vendedor
	private Integer precioMinVenta;
	// Tiempo máximo de compra
	private Integer tiempoMaxCompra;
	// Tiempo máximo de compra cumplido
	private boolean tiempoCompraFinalizado = true;
	// Variable para comprobar si ya ha enviado la orden de compra a un agente
	private boolean comprandoLibro;
	// The list of known seller agents
	private AID[] sellerAgents;

	// Mensaje para la gestión de peticiones del listado de precios
	private ACLMessage listaPreciosRequest = new ACLMessage(ACLMessage.REQUEST);


	// Put agent initializations here
	protected void setup() {
		System.out.println("Hallo! Buyer-agent "+getAID().getName()+" is ready.");

		/**
			Argumentos:
				- el primero es el libro que se quiere comprar
				- el segundo es el número mínimo de precios a consultar (vendedores)
				- el tercero son los minutos máximos que dura la compra
					(en caso de no especificar un tiempo, no se ejecuta el comportamiento de cierre)
		 */
		Object[] args = getArguments();

		if (args != null && args.length > 2) {
			targetBookTitle = (String) args[0];
			System.out.println("Libro objetivo: "+targetBookTitle);	
			numPeticionesVendedores = Integer.parseInt((String) args[1]);
			System.out.println("Nº consultas de precio: "+numPeticionesVendedores);
			precioMax = Integer.parseInt((String) args[2]);
			System.out.println("Precio máximo establecido: "+precioMax);

			// TickerBehaviour para gestionar peticiones a vendedores cada minuto
			addBehaviour(new TickerBehaviour(this, 60000) {
				protected void onTick() {
					System.out.println("Intentando comprar el libro: "+targetBookTitle);
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("book-selling");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						System.out.println("Se han encontrado los siguiente vendedores: ");
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
							System.out.println(sellerAgents[i].getName());
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}

					// MENSAJE REQUEST para enviar una petición de información de precios del libro a todos los vendedores
					//  (el comparado quiere una lista de los precios más bajos por vendedor)
					listaPreciosRequest = new ACLMessage(ACLMessage.REQUEST);
					listaPreciosRequest.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
					listaPreciosRequest.setContent(targetBookTitle);
					listaPreciosRequest.setConversationId(PRICE_LIST_ID);
					listaPreciosRequest.setReplyWith("request-price-books-list"+System.currentTimeMillis()); // Unique value
					for (int i = 0; i < sellerAgents.length; ++i) {
						listaPreciosRequest.addReceiver(sellerAgents[i]);
					}
					myAgent.send(listaPreciosRequest);

					// Perform the request
					myAgent.addBehaviour(new RequestPerformer());
				}
			} );

			// CyclicBehaviour para gestionar el mensaje de INFORM con la información de los precios más bajos
			addBehaviour(new CyclicBehaviour(this) {
				public void action() {
					// Preparamos la plantilla para gestionar la información de la lista de precios más bajos
					// (mesnajes enviados por los vendedores)
					MessageTemplate	mt = MessageTemplate.and(MessageTemplate.MatchConversationId(PRICE_LIST_ID),
							MessageTemplate.MatchInReplyTo(listaPreciosRequest.getReplyWith()));

					ACLMessage msg = myAgent.receive(mt);
					if (msg != null) {
						int price = Integer.parseInt(msg.getContent());

						// actualizamos el precio mínimo en caso de no disponer de uno o en caso de obtener un precio menor
						if (precioMinVenta == null || price < precioMinVenta) {
							precioMinVenta = price;
						}
					}
					else {
						block();
					}
				}
			});

			// en caso de especificar un tiempo máximo de compra, se gestiona el nuevo comportamiento
			if (args.length == 4) {
					// obtenemos el tiempo máximo de compra especificado por el usuario
					tiempoMaxCompra = Integer.parseInt((String) args[3]) * 1000 * 60;
					System.out.println("Tiempo máximo para comprar un libro: "+tiempoMaxCompra+" segundos.");

					// WakerBehaviour para terminar la compra a los X minutos de haber empezado (tiempo límite de compra)
					// Si a los 5 min no se ha realizado ninguna compra, se finaliza el Agente sin haber comprado ningún libro
					addBehaviour(new WakerBehaviour(this, tiempoMaxCompra) {
						protected void handleElapsedTimeout() {
							tiempoCompraFinalizado = true;
							System.out.println("Se ha terminado el tiempo marcado para realiazr la compra...");
							// si está realizando una compra no se hace nada y se espera
							if (!comprandoLibro) {
								myAgent.doDelete();
							}
						}
					} );
			}
		}
		else {
			// Make the agent terminate
			System.out.println("No se ha especificado los argumentos obligatorios: libro a comprar, nº de consultas de precios y el precio máximo!");
			doDelete();
		}
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
	}

	/**
	   Inner class RequestPerformer.
	   This is the behaviour used by Book-buyer agents to request seller 
	   agents the target book.
	 */
	private class RequestPerformer extends Behaviour {
		private AID bestSeller; // The agent who provides the best offer 
		private int bestPrice;  // The best offered price
		private int offersCnt = 0; // contador de ofertas por parte de los vendedores
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public void action() {
			// acciones realizadas por el comportamiento de gestión de peticiones
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				} 
				cfp.setContent(targetBookTitle);
				cfp.setConversationId(BOOK_TRADING_ID);
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId(BOOK_TRADING_ID),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from seller agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						int price = Integer.parseInt(reply.getContent());

						// solo consideramos ofertas inferiores a un precio máximo establecido
						if (price < precioMax) {
							if (bestSeller == null || price < bestPrice) {
								// This is the best offer at present
								bestPrice = price;
								bestSeller = reply.getSender();
							}

							offersCnt++;
						}
					}

					// esperamos a recibir tres ofertas
					if (offersCnt >= numPeticionesVendedores) {
						step = 2; 
						// si el mejor precio es menor que el menor precio por el que se ha comprado ese libro, se compra
						if (bestPrice < precioMinVenta) {
							step = 2; 
						} else {
							// en caso de que el precio más bajo ofertado por los vendedores sea superior al mínimo histórico,
							// si la diferencia en el incremento supone menos de un 35%, se realiza la compra
							if(((bestPrice - precioMinVenta) / (precioMinVenta * 1.0)) <= 0.35) {
								step = 2;
							} else {
								System.out.println("Las ofertas recibidas son muy superiores a los precios de ventas anteriores!");
								myAgent.doDelete();
							}

							// ALEATORIO: el 50% de las veces lo compra sin importar la diferencia
							// if(Math.random() < 0.5){
							// 	step = 2; 
							// } else {
							// 	System.out.println("Las ofertas recibidas son muy superiores a los precios de ventas anteriores!");
							// 	myAgent.doDelete();
							// }
						}
					} else {
						System.out.println("Todavía me falta por recibir "+(numPeticionesVendedores - offersCnt)+" ofertas!");
					}
				}
				else {
					block();
				}
				break;
			case 2:
				comprandoLibro = true;
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(targetBookTitle);
				order.setConversationId(BOOK_TRADING_ID);
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId(BOOK_TRADING_ID),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
			case 3:      
				comprandoLibro = false;
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println(targetBookTitle+" successfully purchased from agent "+reply.getSender().getName());
						System.out.println("Price = "+bestPrice);
						myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: requested book already sold.");
						// si se ha terminado el tiempo de compra, dejamos de comprar
						if(tiempoCompraFinalizado) myAgent.doDelete();
					}

					step = 4;
				}
				else {
					block();
				}
				break;
			}        
		}

		public boolean done() {
			if (step == 2 && bestSeller == null) {
				System.out.println("Attempt failed: "+targetBookTitle+" not available for sale");
			}
			return ((step == 2 && bestSeller == null) || step == 4);
		}
	}  // End of inner class RequestPerformer
}
