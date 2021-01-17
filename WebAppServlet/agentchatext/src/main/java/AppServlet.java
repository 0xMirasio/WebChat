/*Bonsoir, 
Il y a un souci avec la procédure que vous décrivez dans le document. Les machines 
à l'extérieur du réseau R ne peuvent pas prendre l'initiative et contacter une machine 
du réseau directement (et vice versa). C'est une contrainte forte et en contradiction 
avec ce qui est demandé. 
C'est pour cela qu'on vous a proposé de passer par une servlet centralisée, où 
toutes les informations nécessaires seront collectées. Typiquement, chaque machine 
fait appel à la méthode subscribe() de la servlet au moment de rejoindre le réseau. 
Cette méthode permet d'établir la liste de toutes la machines + leur situation 
(à l'intérieur / à l'extérieur) + leurs coordonnées (IP, port). 
Quand une machine change d'état (e.g. passage de offline vers online) elle utilise 
la méthode update(). Dans ce cas là, le serveur met à jour les informations 
relatives à cette machine dans sa liste puis diffuse l'info à tous les autres 
machines de la liste en utilisant la troisième et dernière méthode notify(). 
J'espère que c'est plus clair ainsi.*/


import java.io.IOException;
//import java.io.PrintWriter;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/hello")
public class AppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//Récupération de la liste des users déjà connecté depuis le réseau interne de 
		//l'entreprise
		
		


		//La liste des utilisateurs connectés a été récupéré.

		String message = "Transmission de la liste des utilisataurs connectés : OK!";
		request.setAttribute("test", message);
		this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		

	}
		//Envoie de l'inscription d'une machine de l'extérieur à H

		//Les paramètres envoyé je dois récupérer le nom de l'users
		//Stocker les noms dans une liste et cette liste doit être publique
		//
	
	  protected void doPost(HttpServletRequest request, HttpServletResponse
	  response) throws ServletException, IOException { doGet(request, response); }
	 

}
